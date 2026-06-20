package za.co.patrick.finance.budget.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BudgetSummaryView(
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
