package za.co.patrick.finance.cashflow.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import za.co.patrick.finance.cashflow.domain.IncomeFrequency;

public record CreateIncomeEntryCommand(
        UUID profileId,
        String sourceName,
        BigDecimal amount,
        String currency,
        IncomeFrequency frequency,
        LocalDate startDate,
        LocalDate endDate,
        boolean taxable
) {
}
