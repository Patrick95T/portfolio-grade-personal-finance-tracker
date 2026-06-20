package za.co.patrick.finance.cashflow.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import za.co.patrick.finance.cashflow.domain.ExpenseCategory;
import za.co.patrick.finance.cashflow.domain.ExpenseFrequency;

public record CreateExpenseEntryRequest(
        @Schema(example = "Rent")
        @NotBlank(message = "expenseName is required")
        String expenseName,
        @Schema(example = "1500.00")
        @NotNull(message = "amount is required")
        @DecimalMin(value = "0.00", message = "amount must be zero or greater")
        BigDecimal amount,
        @Schema(example = "USD")
        @Pattern(regexp = "^[A-Z]{3}$", message = "currency must be a 3-letter ISO code")
        String currency,
        @Schema(example = "HOUSING")
        @NotNull(message = "category is required")
        ExpenseCategory category,
        @Schema(example = "MONTHLY")
        @NotNull(message = "frequency is required")
        ExpenseFrequency frequency,
        @Schema(example = "2026-07-01")
        @NotNull(message = "startDate is required")
        LocalDate startDate,
        @Schema(example = "2026-12-31")
        LocalDate endDate,
        @Schema(example = "true")
        boolean essential
) {
}
