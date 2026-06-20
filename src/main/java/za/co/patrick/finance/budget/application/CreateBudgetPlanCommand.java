package za.co.patrick.finance.budget.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateBudgetPlanCommand(
        UUID profileId,
        LocalDate monthStart,
        BigDecimal housingTarget,
        BigDecimal essentialsTarget,
        BigDecimal lifestyleTarget
) {
}
