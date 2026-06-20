package za.co.patrick.finance.debt.api;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DebtPayoffMonthResponse(
        LocalDate month,
        BigDecimal openingBalance,
        BigDecimal interestAccrued,
        BigDecimal paymentApplied,
        BigDecimal closingBalance
) {
}
