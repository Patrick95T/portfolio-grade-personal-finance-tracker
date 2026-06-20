package za.co.patrick.finance.cashflow.application;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.patrick.finance.audit.application.AuditEventService;
import za.co.patrick.finance.audit.application.AuditEventType;
import za.co.patrick.finance.cashflow.domain.IncomeEntry;
import za.co.patrick.finance.profile.application.UserProfileService;
import za.co.patrick.finance.shared.domain.Money;

@Service
@Transactional
public class IncomeEntryService {

    private final IncomeEntryRepository incomeEntryRepository;
    private final UserProfileService userProfileService;
    private final AuditEventService auditEventService;
    private final Clock clock;

    public IncomeEntryService(
            IncomeEntryRepository incomeEntryRepository,
            UserProfileService userProfileService,
            AuditEventService auditEventService,
            Clock clock
    ) {
        this.incomeEntryRepository = incomeEntryRepository;
        this.userProfileService = userProfileService;
        this.auditEventService = auditEventService;
        this.clock = clock;
    }

    public IncomeEntry createIncome(CreateIncomeEntryCommand command) {
        var profile = userProfileService.getProfile(command.profileId());
        Instant now = Instant.now(clock);
        IncomeEntry incomeEntry = new IncomeEntry(
                UUID.randomUUID(),
                command.profileId(),
                command.sourceName().trim(),
                new Money(command.amount(), command.currency() != null ? command.currency() : profile.preferredCurrency()),
                command.frequency(),
                command.startDate(),
                command.endDate(),
                command.taxable(),
                now,
                now
        );
        var savedIncome = incomeEntryRepository.save(incomeEntry);
        auditEventService.record(
                savedIncome.profileId(),
                AuditEventType.INCOME_CREATED,
                "IncomeEntry",
                savedIncome.id(),
                "Created income entry " + savedIncome.sourceName()
        );
        return savedIncome;
    }

    @Transactional(readOnly = true)
    public List<IncomeEntry> listProfileIncome(UUID profileId) {
        userProfileService.getProfile(profileId);
        return incomeEntryRepository.findByProfileId(profileId);
    }
}
