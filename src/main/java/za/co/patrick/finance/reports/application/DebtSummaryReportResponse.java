package za.co.patrick.finance.reports.application;

import java.math.BigDecimal;
import java.util.UUID;

public record DebtSummaryReportResponse(
        UUID profileId,
        int debtCount,
        BigDecimal totalDebtBalance,
        BigDecimal totalMinimumMonthlyPayment,
        BigDecimal weightedAverageInterestRate
) {
}
