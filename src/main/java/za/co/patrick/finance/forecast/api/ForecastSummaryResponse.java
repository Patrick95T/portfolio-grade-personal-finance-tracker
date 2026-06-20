package za.co.patrick.finance.forecast.api;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ForecastSummaryResponse(
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
