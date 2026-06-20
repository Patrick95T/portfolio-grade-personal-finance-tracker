package za.co.patrick.finance.budget.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import za.co.patrick.finance.budget.domain.BudgetPlan;

public interface BudgetPlanRepository {

    BudgetPlan save(BudgetPlan budgetPlan);

    List<BudgetPlan> findByProfileId(UUID profileId);

    Optional<BudgetPlan> findById(UUID budgetPlanId);
}
