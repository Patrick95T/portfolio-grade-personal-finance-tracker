package za.co.patrick.finance.budget.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface BudgetPlanJpaRepository extends JpaRepository<BudgetPlanEntity, UUID> {

    List<BudgetPlanEntity> findByProfileIdOrderByMonthStartAsc(UUID profileId);
}
