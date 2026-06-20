package za.co.patrick.finance.budget.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import za.co.patrick.finance.shared.domain.BusinessRuleViolationException;
import za.co.patrick.finance.shared.domain.Money;

public record BudgetPlan(
        UUID id,
        UUID profileId,
        LocalDate monthStart,
        Money housingTarget,
        Money essentialsTarget,
        Money lifestyleTarget,
        Instant createdAt,
        Instant updatedAt
) {

    public BudgetPlan {
        Objects.requireNonNull(id, "id is required");
        Objects.requireNonNull(profileId, "profileId is required");
        Objects.requireNonNull(monthStart, "monthStart is required");
        Objects.requireNonNull(housingTarget, "housingTarget is required");
        Objects.requireNonNull(essentialsTarget, "essentialsTarget is required");
        Objects.requireNonNull(lifestyleTarget, "lifestyleTarget is required");
        Objects.requireNonNull(createdAt, "createdAt is required");
        Objects.requireNonNull(updatedAt, "updatedAt is required");

        if (monthStart.getDayOfMonth() != 1) {
            throw new BusinessRuleViolationException("Budget month must start on the first day of the month");
        }
    }
}
