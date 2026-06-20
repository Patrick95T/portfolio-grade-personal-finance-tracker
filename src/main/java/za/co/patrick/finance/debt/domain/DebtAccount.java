package za.co.patrick.finance.debt.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import za.co.patrick.finance.shared.domain.Money;

public record DebtAccount(
        UUID id,
        UUID profileId,
        String lenderName,
        DebtType debtType,
        Money currentBalance,
        Money minimumMonthlyPayment,
        BigDecimal annualInterestRate,
        Instant createdAt,
        Instant updatedAt
) {
    public DebtAccount {
        Objects.requireNonNull(id);
        Objects.requireNonNull(profileId);
        Objects.requireNonNull(lenderName);
        Objects.requireNonNull(debtType);
        Objects.requireNonNull(currentBalance);
        Objects.requireNonNull(minimumMonthlyPayment);
        Objects.requireNonNull(annualInterestRate);
        Objects.requireNonNull(createdAt);
        Objects.requireNonNull(updatedAt);
    }
}
