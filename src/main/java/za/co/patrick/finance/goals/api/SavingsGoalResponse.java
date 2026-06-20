package za.co.patrick.finance.goals.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import za.co.patrick.finance.goals.domain.GoalType;

public record SavingsGoalResponse(
        UUID id,
        UUID profileId,
        String goalName,
        GoalType goalType,
        BigDecimal targetAmount,
        BigDecimal currentSaved,
        LocalDate targetDate,
        Instant createdAt,
        Instant updatedAt
) {
}
