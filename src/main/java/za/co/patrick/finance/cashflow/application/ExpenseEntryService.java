package za.co.patrick.finance.cashflow.application;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.patrick.finance.audit.application.AuditEventService;
import za.co.patrick.finance.audit.application.AuditEventType;
import za.co.patrick.finance.cashflow.domain.ExpenseEntry;
import za.co.patrick.finance.profile.application.UserProfileService;
import za.co.patrick.finance.shared.domain.Money;

@Service
@Transactional
public class ExpenseEntryService {

    private final ExpenseEntryRepository expenseEntryRepository;
    private final UserProfileService userProfileService;
    private final AuditEventService auditEventService;
    private final Clock clock;

    public ExpenseEntryService(
            ExpenseEntryRepository expenseEntryRepository,
            UserProfileService userProfileService,
            AuditEventService auditEventService,
            Clock clock
    ) {
        this.expenseEntryRepository = expenseEntryRepository;
        this.userProfileService = userProfileService;
        this.auditEventService = auditEventService;
        this.clock = clock;
    }

    public ExpenseEntry createExpense(CreateExpenseEntryCommand command) {
        var profile = userProfileService.getProfile(command.profileId());
        Instant now = Instant.now(clock);
        ExpenseEntry expenseEntry = new ExpenseEntry(
                UUID.randomUUID(),
                command.profileId(),
                command.expenseName().trim(),
                new Money(command.amount(), command.currency() != null ? command.currency() : profile.preferredCurrency()),
                command.category(),
                command.frequency(),
                command.startDate(),
                command.endDate(),
                command.essential(),
                now,
                now
        );
        var savedExpense = expenseEntryRepository.save(expenseEntry);
        auditEventService.record(
                savedExpense.profileId(),
                AuditEventType.EXPENSE_CREATED,
                "ExpenseEntry",
                savedExpense.id(),
                "Created expense entry " + savedExpense.expenseName()
        );
        return savedExpense;
    }

    @Transactional(readOnly = true)
    public List<ExpenseEntry> listProfileExpenses(UUID profileId) {
        userProfileService.getProfile(profileId);
        return expenseEntryRepository.findByProfileId(profileId);
    }

    @Transactional(readOnly = true)
    public List<ExpenseEntry> listProfileExpensesForMonth(UUID profileId, LocalDate monthStart) {
        userProfileService.getProfile(profileId);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
        return expenseEntryRepository.findByProfileIdAndMonth(profileId, monthStart, monthEnd);
    }
}
