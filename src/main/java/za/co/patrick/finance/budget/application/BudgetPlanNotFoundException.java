package za.co.patrick.finance.budget.application;

import java.util.UUID;

public final class BudgetPlanNotFoundException extends RuntimeException {

    public BudgetPlanNotFoundException(UUID budgetPlanId) {
        super("Budget plan with id '%s' was not found".formatted(budgetPlanId));
    }
}
