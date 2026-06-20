package za.co.patrick.finance.goals.persistence;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import za.co.patrick.finance.goals.domain.GoalType;

@Entity
@Table(name = "savings_goals")
public class SavingsGoalEntity {
    @Id
    private UUID id;
    @Column(name = "profile_id", nullable = false)
    private UUID profileId;
    @Column(name = "goal_name", nullable = false, length = 120)
    private String goalName;
    @Enumerated(EnumType.STRING)
    @Column(name = "goal_type", nullable = false, length = 30)
    private GoalType goalType;
    @Column(name = "target_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal targetAmount;
    @Column(name = "current_saved", nullable = false, precision = 19, scale = 2)
    private BigDecimal currentSaved;
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;
    @Column(name = "target_date")
    private LocalDate targetDate;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    protected SavingsGoalEntity() {}
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getProfileId() { return profileId; }
    public void setProfileId(UUID profileId) { this.profileId = profileId; }
    public String getGoalName() { return goalName; }
    public void setGoalName(String goalName) { this.goalName = goalName; }
    public GoalType getGoalType() { return goalType; }
    public void setGoalType(GoalType goalType) { this.goalType = goalType; }
    public BigDecimal getTargetAmount() { return targetAmount; }
    public void setTargetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; }
    public BigDecimal getCurrentSaved() { return currentSaved; }
    public void setCurrentSaved(BigDecimal currentSaved) { this.currentSaved = currentSaved; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public LocalDate getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDate targetDate) { this.targetDate = targetDate; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
