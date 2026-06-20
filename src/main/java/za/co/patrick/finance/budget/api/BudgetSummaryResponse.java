package za.co.patrick.finance.budget.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BudgetSummaryResponse(
        UUID budgetPlanId,
        LocalDate monthStart,
        BigDecimal plannedHousing,
        BigDecimal actualHousing,
        BigDecimal plannedEssentials,
        BigDecimal actualEssentials,
        BigDecimal plannedLifestyle,
        BigDecimal actualLifestyle
) {
}
