package za.co.patrick.finance.debt.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public record DebtPayoffSimulationRequest(
        @Schema(example = "80.00")
        @DecimalMin(value = "0.00") BigDecimal extraMonthlyPayment
) {
}
