package za.co.patrick.finance.forecast.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ForecastSummary(
        BigDecimal startingNetWorth,
        BigDecimal endingAssets,
        BigDecimal endingDebt,
        BigDecimal endingNetWorth,
        BigDecimal totalInterestAccrued,
        BigDecimal endingGoalSaved,
        LocalDate projectedDebtFreeMonth,
        LocalDate projectedGoalCompletionMonth
) {
}
