package za.co.patrick.finance.goals.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SavingsGoalProjectionResponse(
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
