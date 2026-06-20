package za.co.patrick.finance.debt.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import za.co.patrick.finance.debt.domain.DebtType;

public record DebtAccountResponse(
        UUID id,
        UUID profileId,
        String lenderName,
        DebtType debtType,
        BigDecimal currentBalance,
        BigDecimal minimumMonthlyPayment,
        BigDecimal annualInterestRate,
        Instant createdAt,
        Instant updatedAt
) {
}
