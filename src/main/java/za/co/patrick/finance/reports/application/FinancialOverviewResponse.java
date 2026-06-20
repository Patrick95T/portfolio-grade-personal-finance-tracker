package za.co.patrick.finance.reports.application;

import java.math.BigDecimal;
import java.util.UUID;

public record FinancialOverviewResponse(
        UUID profileId,
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal monthlySurplus,
        BigDecimal totalDebt,
        BigDecimal totalAssets,
        BigDecimal netWorth,
        BigDecimal totalGoalTarget,
        BigDecimal totalGoalSaved
) {
}
