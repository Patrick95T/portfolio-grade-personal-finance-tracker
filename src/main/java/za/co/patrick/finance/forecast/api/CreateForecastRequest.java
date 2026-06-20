package za.co.patrick.finance.forecast.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateForecastRequest(
        @Schema(example = "2026-07-01")
        @NotNull LocalDate startMonth,
        @Schema(example = "12")
        @Min(1) @Max(60) int months,
        @Schema(example = "80.00")
        BigDecimal extraDebtPayment,
        @Schema(example = "500.00")
        BigDecimal monthlySavingsContribution,
        @Schema(example = "0.00")
        BigDecimal monthlyIncomeAdjustment,
        @Schema(example = "0.00")
        BigDecimal monthlyExpenseAdjustment
) {
}
