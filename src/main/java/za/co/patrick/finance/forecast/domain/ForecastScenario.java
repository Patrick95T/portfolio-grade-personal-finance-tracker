package za.co.patrick.finance.forecast.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ForecastScenario(
        LocalDate startMonth,
        int months,
        BigDecimal extraDebtPayment,
        BigDecimal monthlySavingsContribution,
        BigDecimal monthlyIncomeAdjustment,
        BigDecimal monthlyExpenseAdjustment
) {
}
