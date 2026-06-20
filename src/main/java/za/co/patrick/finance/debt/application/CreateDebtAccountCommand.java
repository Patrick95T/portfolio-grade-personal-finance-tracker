package za.co.patrick.finance.debt.application;

import java.math.BigDecimal;
import java.util.UUID;
import za.co.patrick.finance.debt.domain.DebtType;

public record CreateDebtAccountCommand(
        UUID profileId,
        String lenderName,
        DebtType debtType,
        BigDecimal currentBalance,
        BigDecimal minimumMonthlyPayment,
        BigDecimal annualInterestRate
) {
}
