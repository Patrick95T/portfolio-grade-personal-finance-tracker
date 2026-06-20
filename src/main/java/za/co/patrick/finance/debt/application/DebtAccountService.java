package za.co.patrick.finance.debt.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.patrick.finance.audit.application.AuditEventService;
import za.co.patrick.finance.audit.application.AuditEventType;
import za.co.patrick.finance.debt.domain.DebtAccount;
import za.co.patrick.finance.profile.application.UserProfileService;
import za.co.patrick.finance.shared.domain.BusinessRuleViolationException;
import za.co.patrick.finance.shared.domain.Money;

@Service
@Transactional
public class DebtAccountService {
    private final DebtAccountRepository debtAccountRepository;
    private final UserProfileService userProfileService;
    private final AuditEventService auditEventService;
    private final Clock clock;

    public DebtAccountService(
            DebtAccountRepository debtAccountRepository,
            UserProfileService userProfileService,
            AuditEventService auditEventService,
            Clock clock
    ) {
        this.debtAccountRepository = debtAccountRepository;
        this.userProfileService = userProfileService;
        this.auditEventService = auditEventService;
        this.clock = clock;
    }

    public DebtAccount createDebtAccount(CreateDebtAccountCommand command) {
        var profile = userProfileService.getProfile(command.profileId());
        Instant now = Instant.now(clock);
        var debt = new DebtAccount(
                UUID.randomUUID(),
                command.profileId(),
                command.lenderName().trim(),
                command.debtType(),
                new Money(command.currentBalance(), profile.preferredCurrency()),
                new Money(command.minimumMonthlyPayment(), profile.preferredCurrency()),
                command.annualInterestRate(),
                now,
                now
        );
        var savedDebt = debtAccountRepository.save(debt);
        auditEventService.record(
                savedDebt.profileId(),
                AuditEventType.DEBT_CREATED,
                "DebtAccount",
                savedDebt.id(),
                "Created debt account " + savedDebt.lenderName()
        );
        return savedDebt;
    }

    @Transactional(readOnly = true)
    public List<DebtAccount> listDebtAccounts(UUID profileId) {
        userProfileService.getProfile(profileId);
        return debtAccountRepository.findByProfileId(profileId);
    }

    @Transactional(readOnly = true)
    public DebtPayoffProjection simulatePayoff(UUID profileId, UUID debtAccountId, BigDecimal extraMonthlyPayment) {
        userProfileService.getProfile(profileId);
        if (extraMonthlyPayment.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleViolationException("Extra monthly payment cannot be negative");
        }

        var debt = debtAccountRepository.findById(debtAccountId)
                .filter(account -> account.profileId().equals(profileId))
                .orElseThrow(() -> new DebtAccountNotFoundException(debtAccountId));

        BigDecimal balance = debt.currentBalance().amount();
        BigDecimal totalPayment = debt.minimumMonthlyPayment().amount().add(extraMonthlyPayment).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalInterest = BigDecimal.ZERO;
        List<DebtPayoffMonth> schedule = new ArrayList<>();
        LocalDate month = LocalDate.now(clock).withDayOfMonth(1);
        int payoffMonths = 0;

        while (balance.compareTo(BigDecimal.ZERO) > 0 && payoffMonths < 600) {
            BigDecimal openingBalance = balance;
            BigDecimal interest = openingBalance.multiply(debt.annualInterestRate())
                    .divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            BigDecimal balanceAfterInterest = openingBalance.add(interest);
            BigDecimal payment = totalPayment.min(balanceAfterInterest).setScale(2, RoundingMode.HALF_UP);
            BigDecimal closingBalance = balanceAfterInterest.subtract(payment).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);

            schedule.add(new DebtPayoffMonth(month, openingBalance.setScale(2, RoundingMode.HALF_UP), interest, payment, closingBalance));
            totalInterest = totalInterest.add(interest);
            balance = closingBalance;
            month = month.plusMonths(1);
            payoffMonths++;
        }

        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessRuleViolationException("Debt payoff simulation exceeded the supported horizon");
        }

        return new DebtPayoffProjection(
                debt.id(),
                debt.profileId(),
                debt.lenderName(),
                debt.currentBalance().amount(),
                debt.minimumMonthlyPayment().amount(),
                extraMonthlyPayment.setScale(2, RoundingMode.HALF_UP),
                totalPayment,
                totalInterest.setScale(2, RoundingMode.HALF_UP),
                payoffMonths,
                schedule.getLast().month(),
                schedule
        );
    }
}
