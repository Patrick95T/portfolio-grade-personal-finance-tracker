package za.co.patrick.finance.cashflow.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import za.co.patrick.finance.cashflow.domain.ExpenseCategory;
import za.co.patrick.finance.cashflow.domain.ExpenseFrequency;

public record ExpenseEntryResponse(
        UUID id,
        UUID profileId,
        String expenseName,
        BigDecimal amount,
        String currency,
        ExpenseCategory category,
        ExpenseFrequency frequency,
        LocalDate startDate,
        LocalDate endDate,
        boolean essential,
        Instant createdAt,
        Instant updatedAt
) {
}
