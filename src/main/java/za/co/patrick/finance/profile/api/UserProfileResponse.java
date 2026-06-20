package za.co.patrick.finance.profile.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import za.co.patrick.finance.profile.domain.EmploymentType;

public record UserProfileResponse(
        UUID id,
        String fullName,
        String emailAddress,
        EmploymentType employmentType,
        String preferredCurrency,
        String countryCode,
        BigDecimal monthlyNetIncome,
        BigDecimal monthlyHousingCost,
        BigDecimal monthlyEssentialCosts,
        BigDecimal monthlyAvailableForPlanning,
        LocalDate onboardingDate,
        Instant createdAt,
        Instant updatedAt
) {
}
