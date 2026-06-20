package za.co.patrick.finance.debt.application;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DebtPayoffMonth(
        LocalDate month,
        BigDecimal openingBalance,
        BigDecimal interestAccrued,
        BigDecimal paymentApplied,
        BigDecimal closingBalance
) {
}
