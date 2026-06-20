package za.co.patrick.finance.profile.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import za.co.patrick.finance.profile.domain.EmploymentType;

public record CreateUserProfileRequest(
        @Schema(example = "Patrick Tester")
        @NotBlank(message = "fullName is required")
        String fullName,
        @Schema(example = "patrick@example.com")
        @NotBlank(message = "emailAddress is required")
        @Email(message = "emailAddress must be a valid email address")
        String emailAddress,
        @Schema(example = "SALARIED")
        @NotNull(message = "employmentType is required")
        EmploymentType employmentType,
        @Schema(example = "ZAR")
        @NotBlank(message = "preferredCurrency is required")
        @Pattern(regexp = "^[A-Z]{3}$", message = "preferredCurrency must be a 3-letter ISO code")
        String preferredCurrency,
        @Schema(example = "ZA")
        @NotBlank(message = "countryCode is required")
        @Pattern(regexp = "^[A-Z]{2}$", message = "countryCode must be a 2-letter ISO code")
        String countryCode,
        @Schema(example = "45000.00")
        @NotNull(message = "monthlyNetIncome is required")
        @DecimalMin(value = "0.00", message = "monthlyNetIncome must be zero or greater")
        BigDecimal monthlyNetIncome,
        @Schema(example = "12000.00")
        @NotNull(message = "monthlyHousingCost is required")
        @DecimalMin(value = "0.00", message = "monthlyHousingCost must be zero or greater")
        BigDecimal monthlyHousingCost,
        @Schema(example = "8000.00")
        @NotNull(message = "monthlyEssentialCosts is required")
        @DecimalMin(value = "0.00", message = "monthlyEssentialCosts must be zero or greater")
        BigDecimal monthlyEssentialCosts,
        @Schema(example = "2026-06-20")
        @NotNull(message = "onboardingDate is required")
        LocalDate onboardingDate
) {
}
