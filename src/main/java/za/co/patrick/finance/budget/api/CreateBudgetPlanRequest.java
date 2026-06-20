package za.co.patrick.finance.budget.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateBudgetPlanRequest(
        @Schema(example = "2026-07-01")
        @NotNull(message = "monthStart is required")
        LocalDate monthStart,
        @Schema(example = "1500.00")
        @NotNull(message = "housingTarget is required")
        @DecimalMin(value = "0.00", message = "housingTarget must be zero or greater")
        BigDecimal housingTarget,
        @Schema(example = "900.00")
        @NotNull(message = "essentialsTarget is required")
        @DecimalMin(value = "0.00", message = "essentialsTarget must be zero or greater")
        BigDecimal essentialsTarget,
        @Schema(example = "600.00")
        @NotNull(message = "lifestyleTarget is required")
        @DecimalMin(value = "0.00", message = "lifestyleTarget must be zero or greater")
        BigDecimal lifestyleTarget
) {
}
