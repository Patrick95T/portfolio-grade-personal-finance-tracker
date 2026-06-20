package za.co.patrick.finance.forecast.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record GenerateForecastCommand(
        UUID profileId,
        LocalDate startMonth,
        int months,
        BigDecimal extraDebtPayment,
        BigDecimal monthlySavingsContribution,
        BigDecimal monthlyIncomeAdjustment,
        BigDecimal monthlyExpenseAdjustment
) {
}
