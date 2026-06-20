package za.co.patrick.finance.goals.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SavingsGoalProjection(
        UUID goalId,
        UUID profileId,
        String goalName,
        BigDecimal currentSaved,
        BigDecimal targetAmount,
        BigDecimal remainingGap,
        BigDecimal monthlyContribution,
        BigDecimal requiredMonthlyContribution,
        int projectedMonthsToGoal,
        LocalDate projectedCompletionMonth,
        boolean onTrackForTargetDate
) {
}
