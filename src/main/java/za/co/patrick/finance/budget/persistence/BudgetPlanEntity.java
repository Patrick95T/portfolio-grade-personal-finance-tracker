package za.co.patrick.finance.budget.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "budget_plans")
public class BudgetPlanEntity {

    @Id
    private UUID id;
    @Column(name = "profile_id", nullable = false)
    private UUID profileId;
    @Column(name = "month_start", nullable = false)
    private LocalDate monthStart;
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;
    @Column(name = "housing_target", nullable = false, precision = 19, scale = 2)
    private BigDecimal housingTarget;
    @Column(name = "essentials_target", nullable = false, precision = 19, scale = 2)
    private BigDecimal essentialsTarget;
    @Column(name = "lifestyle_target", nullable = false, precision = 19, scale = 2)
    private BigDecimal lifestyleTarget;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected BudgetPlanEntity() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getProfileId() { return profileId; }
    public void setProfileId(UUID profileId) { this.profileId = profileId; }
    public LocalDate getMonthStart() { return monthStart; }
    public void setMonthStart(LocalDate monthStart) { this.monthStart = monthStart; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public BigDecimal getHousingTarget() { return housingTarget; }
    public void setHousingTarget(BigDecimal housingTarget) { this.housingTarget = housingTarget; }
    public BigDecimal getEssentialsTarget() { return essentialsTarget; }
    public void setEssentialsTarget(BigDecimal essentialsTarget) { this.essentialsTarget = essentialsTarget; }
    public BigDecimal getLifestyleTarget() { return lifestyleTarget; }
    public void setLifestyleTarget(BigDecimal lifestyleTarget) { this.lifestyleTarget = lifestyleTarget; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
