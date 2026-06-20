package za.co.patrick.finance.goals.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import za.co.patrick.finance.goals.domain.GoalType;

public record CreateSavingsGoalRequest(
        @Schema(example = "Emergency Fund")
        @NotBlank(message = "goalName is required")
        String goalName,
        @Schema(example = "EMERGENCY_FUND")
        @NotNull(message = "goalType is required")
        GoalType goalType,
        @Schema(example = "10000.00")
        @NotNull(message = "targetAmount is required")
        @DecimalMin(value = "0.00", message = "targetAmount must be zero or greater")
        BigDecimal targetAmount,
        @Schema(example = "3500.00")
        @NotNull(message = "currentSaved is required")
        @DecimalMin(value = "0.00", message = "currentSaved must be zero or greater")
        BigDecimal currentSaved,
        @Schema(example = "2027-06-01")
        LocalDate targetDate
) {
}
