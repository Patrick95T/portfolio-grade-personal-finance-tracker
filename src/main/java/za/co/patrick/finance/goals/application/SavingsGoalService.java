package za.co.patrick.finance.goals.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.patrick.finance.audit.application.AuditEventService;
import za.co.patrick.finance.audit.application.AuditEventType;
import za.co.patrick.finance.goals.domain.SavingsGoal;
import za.co.patrick.finance.profile.application.UserProfileService;
import za.co.patrick.finance.shared.domain.Money;

@Service
@Transactional
public class SavingsGoalService {
    private final SavingsGoalRepository savingsGoalRepository;
    private final UserProfileService userProfileService;
    private final AuditEventService auditEventService;
    private final Clock clock;

    public SavingsGoalService(
            SavingsGoalRepository savingsGoalRepository,
            UserProfileService userProfileService,
            AuditEventService auditEventService,
            Clock clock
    ) {
        this.savingsGoalRepository = savingsGoalRepository;
        this.userProfileService = userProfileService;
        this.auditEventService = auditEventService;
        this.clock = clock;
    }

    public SavingsGoal createSavingsGoal(CreateSavingsGoalCommand command) {
        var profile = userProfileService.getProfile(command.profileId());
        Instant now = Instant.now(clock);
        var goal = new SavingsGoal(
                UUID.randomUUID(),
                command.profileId(),
                command.goalName().trim(),
                command.goalType(),
                new Money(command.targetAmount(), profile.preferredCurrency()),
                new Money(command.currentSaved(), profile.preferredCurrency()),
                command.targetDate(),
                now,
                now
        );
        var savedGoal = savingsGoalRepository.save(goal);
        auditEventService.record(
                savedGoal.profileId(),
                AuditEventType.GOAL_CREATED,
                "SavingsGoal",
                savedGoal.id(),
                "Created savings goal " + savedGoal.goalName()
        );
        return savedGoal;
    }

    @Transactional(readOnly = true)
    public List<SavingsGoal> listSavingsGoals(UUID profileId) {
        userProfileService.getProfile(profileId);
        return savingsGoalRepository.findByProfileId(profileId);
    }

    @Transactional(readOnly = true)
    public SavingsGoalProjection projectGoal(UUID profileId, UUID goalId, BigDecimal monthlyContribution) {
        var profile = userProfileService.getProfile(profileId);
        var goal = savingsGoalRepository.findById(goalId)
                .filter(savedGoal -> savedGoal.profileId().equals(profileId))
                .orElseThrow(() -> new SavingsGoalNotFoundException(goalId));

        BigDecimal contribution = (monthlyContribution == null
                ? profile.monthlyAvailableForPlanning().amount()
                : monthlyContribution).setScale(2, RoundingMode.HALF_UP);

        BigDecimal remainingGap = goal.targetAmount().amount().subtract(goal.currentSaved().amount()).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        int projectedMonths = contribution.compareTo(BigDecimal.ZERO) > 0
                ? remainingGap.divide(contribution, 0, RoundingMode.CEILING).intValue()
                : Integer.MAX_VALUE;
        LocalDate projectedCompletionMonth = contribution.compareTo(BigDecimal.ZERO) > 0
                ? LocalDate.now(clock).withDayOfMonth(1).plusMonths(projectedMonths == 0 ? 0 : projectedMonths - 1L)
                : null;

        BigDecimal requiredContribution = BigDecimal.ZERO;
        boolean onTrackForTargetDate = goal.targetDate() == null;
        if (goal.targetDate() != null) {
            long monthsUntilTarget = Math.max(1, ChronoUnit.MONTHS.between(
                    LocalDate.now(clock).withDayOfMonth(1),
                    goal.targetDate().withDayOfMonth(1)
            ) + 1);
            requiredContribution = remainingGap.divide(BigDecimal.valueOf(monthsUntilTarget), 2, RoundingMode.CEILING);
            onTrackForTargetDate = contribution.compareTo(requiredContribution) >= 0;
        }

        return new SavingsGoalProjection(
                goal.id(),
                goal.profileId(),
                goal.goalName(),
                goal.currentSaved().amount(),
                goal.targetAmount().amount(),
                remainingGap,
                contribution,
                requiredContribution,
                projectedMonths == Integer.MAX_VALUE ? -1 : projectedMonths,
                projectedCompletionMonth,
                onTrackForTargetDate
        );
    }
}
