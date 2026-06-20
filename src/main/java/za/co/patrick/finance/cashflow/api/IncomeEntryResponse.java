package za.co.patrick.finance.cashflow.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import za.co.patrick.finance.cashflow.domain.IncomeFrequency;

public record IncomeEntryResponse(
        UUID id,
        UUID profileId,
        String sourceName,
        BigDecimal amount,
        String currency,
        IncomeFrequency frequency,
        LocalDate startDate,
        LocalDate endDate,
        boolean taxable,
        Instant createdAt,
        Instant updatedAt
) {
}
