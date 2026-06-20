package za.co.patrick.finance.forecast.api;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ForecastMonthResponse(
        LocalDate month,
        BigDecimal income,
        BigDecimal expenses,
        BigDecimal debtPayment,
        BigDecimal debtInterestAccrued,
        BigDecimal savingsContribution,
        BigDecimal netCashflowAfterDebt,
        BigDecimal projectedAssets,
        BigDecimal projectedDebt,
        BigDecimal projectedNetWorth,
        BigDecimal totalGoalSaved
) {
}
