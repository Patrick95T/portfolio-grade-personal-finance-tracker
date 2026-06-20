package za.co.patrick.finance.debt.persistence;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import za.co.patrick.finance.debt.domain.DebtType;

@Entity
@Table(name = "debt_accounts")
public class DebtAccountEntity {
    @Id
    private UUID id;
    @Column(name = "profile_id", nullable = false)
    private UUID profileId;
    @Column(name = "lender_name", nullable = false, length = 120)
    private String lenderName;
    @Enumerated(EnumType.STRING)
    @Column(name = "debt_type", nullable = false, length = 30)
    private DebtType debtType;
    @Column(name = "current_balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal currentBalance;
    @Column(name = "minimum_monthly_payment", nullable = false, precision = 19, scale = 2)
    private BigDecimal minimumMonthlyPayment;
    @Column(name = "annual_interest_rate", nullable = false, precision = 8, scale = 4)
    private BigDecimal annualInterestRate;
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    protected DebtAccountEntity() {}
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getProfileId() { return profileId; }
    public void setProfileId(UUID profileId) { this.profileId = profileId; }
    public String getLenderName() { return lenderName; }
    public void setLenderName(String lenderName) { this.lenderName = lenderName; }
    public DebtType getDebtType() { return debtType; }
    public void setDebtType(DebtType debtType) { this.debtType = debtType; }
    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }
    public BigDecimal getMinimumMonthlyPayment() { return minimumMonthlyPayment; }
    public void setMinimumMonthlyPayment(BigDecimal minimumMonthlyPayment) { this.minimumMonthlyPayment = minimumMonthlyPayment; }
    public BigDecimal getAnnualInterestRate() { return annualInterestRate; }
    public void setAnnualInterestRate(BigDecimal annualInterestRate) { this.annualInterestRate = annualInterestRate; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
