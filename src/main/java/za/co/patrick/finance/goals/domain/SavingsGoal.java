package za.co.patrick.finance.goals.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import za.co.patrick.finance.shared.domain.BusinessRuleViolationException;
import za.co.patrick.finance.shared.domain.Money;

public record SavingsGoal(
        UUID id,
        UUID profileId,
        String goalName,
        GoalType goalType,
        Money targetAmount,
        Money currentSaved,
        LocalDate targetDate,
        Instant createdAt,
        Instant updatedAt
) {
    public SavingsGoal {
        Objects.requireNonNull(id);
        Objects.requireNonNull(profileId);
        Objects.requireNonNull(goalName);
        Objects.requireNonNull(goalType);
        Objects.requireNonNull(targetAmount);
        Objects.requireNonNull(currentSaved);
        Objects.requireNonNull(createdAt);
        Objects.requireNonNull(updatedAt);
        if (currentSaved.amount().compareTo(targetAmount.amount()) > 0) {
            throw new BusinessRuleViolationException("Current saved amount cannot exceed the target amount");
        }
    }
}
