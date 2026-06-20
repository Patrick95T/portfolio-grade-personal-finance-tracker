package za.co.patrick.finance.profile.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import za.co.patrick.finance.profile.domain.EmploymentType;

public record CreateUserProfileCommand(
        String fullName,
        String emailAddress,
        EmploymentType employmentType,
        String preferredCurrency,
        String countryCode,
        BigDecimal monthlyNetIncome,
        BigDecimal monthlyHousingCost,
        BigDecimal monthlyEssentialCosts,
        LocalDate onboardingDate
) {
}
