package za.co.patrick.finance.forecast.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ForecastResponse(
        UUID profileId,
        String currency,
        LocalDate startMonth,
        int months,
        BigDecimal extraDebtPayment,
        BigDecimal monthlySavingsContribution,
        BigDecimal monthlyIncomeAdjustment,
        BigDecimal monthlyExpenseAdjustment,
        ForecastSummaryResponse summary,
        List<ForecastMonthResponse> monthlyProjections
) {
}
