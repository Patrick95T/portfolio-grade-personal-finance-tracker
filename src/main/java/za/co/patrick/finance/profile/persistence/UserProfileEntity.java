package za.co.patrick.finance.profile.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import za.co.patrick.finance.profile.domain.EmploymentType;

@Entity
@Table(name = "user_profiles")
public class UserProfileEntity {

    @Id
    private UUID id;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "email_address", nullable = false, length = 150, unique = true)
    private String emailAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false, length = 40)
    private EmploymentType employmentType;

    @Column(name = "preferred_currency", nullable = false, length = 3)
    private String preferredCurrency;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @Column(name = "monthly_net_income", nullable = false, precision = 19, scale = 2)
    private BigDecimal monthlyNetIncome;

    @Column(name = "monthly_housing_cost", nullable = false, precision = 19, scale = 2)
    private BigDecimal monthlyHousingCost;

    @Column(name = "monthly_essential_costs", nullable = false, precision = 19, scale = 2)
    private BigDecimal monthlyEssentialCosts;

    @Column(name = "onboarding_date", nullable = false)
    private LocalDate onboardingDate;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected UserProfileEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public String getPreferredCurrency() {
        return preferredCurrency;
    }

    public void setPreferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public BigDecimal getMonthlyNetIncome() {
        return monthlyNetIncome;
    }

    public void setMonthlyNetIncome(BigDecimal monthlyNetIncome) {
        this.monthlyNetIncome = monthlyNetIncome;
    }

    public BigDecimal getMonthlyHousingCost() {
        return monthlyHousingCost;
    }

    public void setMonthlyHousingCost(BigDecimal monthlyHousingCost) {
        this.monthlyHousingCost = monthlyHousingCost;
    }

    public BigDecimal getMonthlyEssentialCosts() {
        return monthlyEssentialCosts;
    }

    public void setMonthlyEssentialCosts(BigDecimal monthlyEssentialCosts) {
        this.monthlyEssentialCosts = monthlyEssentialCosts;
    }

    public LocalDate getOnboardingDate() {
        return onboardingDate;
    }

    public void setOnboardingDate(LocalDate onboardingDate) {
        this.onboardingDate = onboardingDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
