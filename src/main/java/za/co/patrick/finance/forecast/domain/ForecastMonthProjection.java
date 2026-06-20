package za.co.patrick.finance.forecast.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ForecastMonthProjection(
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
