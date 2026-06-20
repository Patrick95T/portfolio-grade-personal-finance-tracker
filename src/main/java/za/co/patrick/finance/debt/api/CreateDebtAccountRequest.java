package za.co.patrick.finance.debt.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import za.co.patrick.finance.debt.domain.DebtType;

public record CreateDebtAccountRequest(
        @Schema(example = "Bank Loan")
        @NotBlank(message = "lenderName is required")
        String lenderName,
        @Schema(example = "PERSONAL_LOAN")
        @NotNull(message = "debtType is required")
        DebtType debtType,
        @Schema(example = "12000.00")
        @NotNull(message = "currentBalance is required")
        @DecimalMin(value = "0.00", message = "currentBalance must be zero or greater")
        BigDecimal currentBalance,
        @Schema(example = "450.00")
        @NotNull(message = "minimumMonthlyPayment is required")
        @DecimalMin(value = "0.00", message = "minimumMonthlyPayment must be zero or greater")
        BigDecimal minimumMonthlyPayment,
        @Schema(example = "11.50")
        @NotNull(message = "annualInterestRate is required")
        @DecimalMin(value = "0.00", message = "annualInterestRate must be zero or greater")
        BigDecimal annualInterestRate
) {
}
