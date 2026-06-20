package za.co.patrick.finance.cashflow.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import za.co.patrick.finance.shared.domain.BusinessRuleViolationException;
import za.co.patrick.finance.shared.domain.Money;

public record IncomeEntry(
        UUID id,
        UUID profileId,
        String sourceName,
        Money amount,
        IncomeFrequency frequency,
        LocalDate startDate,
        LocalDate endDate,
        boolean taxable,
        Instant createdAt,
        Instant updatedAt
) {

    public IncomeEntry {
        Objects.requireNonNull(id, "id is required");
        Objects.requireNonNull(profileId, "profileId is required");
        Objects.requireNonNull(sourceName, "sourceName is required");
        Objects.requireNonNull(amount, "amount is required");
        Objects.requireNonNull(frequency, "frequency is required");
        Objects.requireNonNull(startDate, "startDate is required");
        Objects.requireNonNull(createdAt, "createdAt is required");
        Objects.requireNonNull(updatedAt, "updatedAt is required");

        if (sourceName.isBlank()) {
            throw new IllegalArgumentException("sourceName must not be blank");
        }

        if (endDate != null && endDate.isBefore(startDate)) {
            throw new BusinessRuleViolationException("Income end date cannot be before the start date");
        }
    }
}
