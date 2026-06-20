package za.co.patrick.finance.debt.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record DebtPayoffProjection(
        UUID debtAccountId,
        UUID profileId,
        String lenderName,
        BigDecimal startingBalance,
        BigDecimal minimumMonthlyPayment,
        BigDecimal extraMonthlyPayment,
        BigDecimal totalMonthlyPayment,
        BigDecimal totalInterestAccrued,
        int payoffMonths,
        LocalDate payoffMonth,
        List<DebtPayoffMonth> monthlySchedule
) {
}
