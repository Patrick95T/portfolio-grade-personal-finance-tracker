package za.co.patrick.finance.goals.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import za.co.patrick.finance.goals.domain.GoalType;

public record CreateSavingsGoalCommand(
        UUID profileId,
        String goalName,
        GoalType goalType,
        BigDecimal targetAmount,
        BigDecimal currentSaved,
        LocalDate targetDate
) {
}
