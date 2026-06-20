package za.co.patrick.finance.reports.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record MonthlyCashflowReportResponse(
        UUID profileId,
        LocalDate month,
        BigDecimal totalIncome,
        BigDecimal housingExpenses,
        BigDecimal essentialExpenses,
        BigDecimal lifestyleExpenses,
        BigDecimal totalExpenses,
        BigDecimal monthlySurplus
) {
}
