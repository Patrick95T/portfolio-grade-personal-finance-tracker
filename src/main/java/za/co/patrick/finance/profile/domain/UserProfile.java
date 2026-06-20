package za.co.patrick.finance.profile.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import za.co.patrick.finance.shared.domain.BusinessRuleViolationException;
import za.co.patrick.finance.shared.domain.Money;

public record UserProfile(
        UUID id,
        String fullName,
        String emailAddress,
        EmploymentType employmentType,
        String preferredCurrency,
        String countryCode,
        Money monthlyNetIncome,
        Money monthlyHousingCost,
        Money monthlyEssentialCosts,
        LocalDate onboardingDate,
        Instant createdAt,
        Instant updatedAt
) {

    public UserProfile {
        Objects.requireNonNull(id, "id is required");
        Objects.requireNonNull(fullName, "fullName is required");
        Objects.requireNonNull(emailAddress, "emailAddress is required");
        Objects.requireNonNull(employmentType, "employmentType is required");
        Objects.requireNonNull(preferredCurrency, "preferredCurrency is required");
        Objects.requireNonNull(countryCode, "countryCode is required");
        Objects.requireNonNull(monthlyNetIncome, "monthlyNetIncome is required");
        Objects.requireNonNull(monthlyHousingCost, "monthlyHousingCost is required");
        Objects.requireNonNull(monthlyEssentialCosts, "monthlyEssentialCosts is required");
        Objects.requireNonNull(onboardingDate, "onboardingDate is required");
        Objects.requireNonNull(createdAt, "createdAt is required");
        Objects.requireNonNull(updatedAt, "updatedAt is required");

        if (monthlyHousingCost.add(monthlyEssentialCosts).amount().compareTo(monthlyNetIncome.amount()) > 0) {
            throw new BusinessRuleViolationException(
                    "Monthly housing and essential costs cannot exceed monthly net income"
            );
        }
    }

    public Money monthlyCommittedCosts() {
        return monthlyHousingCost.add(monthlyEssentialCosts);
    }

    public Money monthlyAvailableForPlanning() {
        return monthlyNetIncome.subtract(monthlyCommittedCosts());
    }
}
