package za.co.patrick.finance.cashflow.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import za.co.patrick.finance.shared.domain.BusinessRuleViolationException;
import za.co.patrick.finance.shared.domain.Money;

public record ExpenseEntry(
        UUID id,
        UUID profileId,
        String expenseName,
        Money amount,
        ExpenseCategory category,
        ExpenseFrequency frequency,
        LocalDate startDate,
        LocalDate endDate,
        boolean essential,
        Instant createdAt,
        Instant updatedAt
) {

    public ExpenseEntry {
        Objects.requireNonNull(id, "id is required");
        Objects.requireNonNull(profileId, "profileId is required");
        Objects.requireNonNull(expenseName, "expenseName is required");
        Objects.requireNonNull(amount, "amount is required");
        Objects.requireNonNull(category, "category is required");
        Objects.requireNonNull(frequency, "frequency is required");
        Objects.requireNonNull(startDate, "startDate is required");
        Objects.requireNonNull(createdAt, "createdAt is required");
        Objects.requireNonNull(updatedAt, "updatedAt is required");

        if (expenseName.isBlank()) {
            throw new IllegalArgumentException("expenseName must not be blank");
        }

        if (endDate != null && endDate.isBefore(startDate)) {
            throw new BusinessRuleViolationException("Expense end date cannot be before the start date");
        }
    }
}
