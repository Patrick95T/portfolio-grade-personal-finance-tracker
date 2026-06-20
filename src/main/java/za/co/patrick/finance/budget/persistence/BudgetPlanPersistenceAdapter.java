package za.co.patrick.finance.budget.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import za.co.patrick.finance.budget.application.BudgetPlanRepository;
import za.co.patrick.finance.budget.domain.BudgetPlan;
import za.co.patrick.finance.shared.domain.Money;

@Repository
public class BudgetPlanPersistenceAdapter implements BudgetPlanRepository {

    private final BudgetPlanJpaRepository budgetPlanJpaRepository;

    public BudgetPlanPersistenceAdapter(BudgetPlanJpaRepository budgetPlanJpaRepository) {
        this.budgetPlanJpaRepository = budgetPlanJpaRepository;
    }

    @Override
    public BudgetPlan save(BudgetPlan budgetPlan) {
        return toDomain(budgetPlanJpaRepository.save(toEntity(budgetPlan)));
    }

    @Override
    public List<BudgetPlan> findByProfileId(UUID profileId) {
        return budgetPlanJpaRepository.findByProfileIdOrderByMonthStartAsc(profileId)
                .stream()
                .map(BudgetPlanPersistenceAdapter::toDomain)
                .toList();
    }

    @Override
    public Optional<BudgetPlan> findById(UUID budgetPlanId) {
        return budgetPlanJpaRepository.findById(budgetPlanId).map(BudgetPlanPersistenceAdapter::toDomain);
    }

    private static BudgetPlanEntity toEntity(BudgetPlan budgetPlan) {
        BudgetPlanEntity entity = new BudgetPlanEntity();
        entity.setId(budgetPlan.id());
        entity.setProfileId(budgetPlan.profileId());
        entity.setMonthStart(budgetPlan.monthStart());
        entity.setCurrency(budgetPlan.housingTarget().currency());
        entity.setHousingTarget(budgetPlan.housingTarget().amount());
        entity.setEssentialsTarget(budgetPlan.essentialsTarget().amount());
        entity.setLifestyleTarget(budgetPlan.lifestyleTarget().amount());
        entity.setCreatedAt(budgetPlan.createdAt());
        entity.setUpdatedAt(budgetPlan.updatedAt());
        return entity;
    }

    private static BudgetPlan toDomain(BudgetPlanEntity entity) {
        return new BudgetPlan(
                entity.getId(),
                entity.getProfileId(),
                entity.getMonthStart(),
                new Money(entity.getHousingTarget(), entity.getCurrency()),
                new Money(entity.getEssentialsTarget(), entity.getCurrency()),
                new Money(entity.getLifestyleTarget(), entity.getCurrency()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
