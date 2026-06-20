package za.co.patrick.finance.goals.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import za.co.patrick.finance.goals.application.SavingsGoalRepository;
import za.co.patrick.finance.goals.domain.SavingsGoal;
import za.co.patrick.finance.shared.domain.Money;

@Repository
public class SavingsGoalPersistenceAdapter implements SavingsGoalRepository {
    private final SavingsGoalJpaRepository savingsGoalJpaRepository;

    public SavingsGoalPersistenceAdapter(SavingsGoalJpaRepository savingsGoalJpaRepository) {
        this.savingsGoalJpaRepository = savingsGoalJpaRepository;
    }

    @Override
    public SavingsGoal save(SavingsGoal savingsGoal) {
        return toDomain(savingsGoalJpaRepository.save(toEntity(savingsGoal)));
    }

    @Override
    public List<SavingsGoal> findByProfileId(UUID profileId) {
        return savingsGoalJpaRepository.findByProfileIdOrderByCreatedAtAsc(profileId).stream().map(SavingsGoalPersistenceAdapter::toDomain).toList();
    }

    @Override
    public Optional<SavingsGoal> findById(UUID savingsGoalId) {
        return savingsGoalJpaRepository.findById(savingsGoalId).map(SavingsGoalPersistenceAdapter::toDomain);
    }

    private static SavingsGoalEntity toEntity(SavingsGoal goal) {
        var entity = new SavingsGoalEntity();
        entity.setId(goal.id());
        entity.setProfileId(goal.profileId());
        entity.setGoalName(goal.goalName());
        entity.setGoalType(goal.goalType());
        entity.setTargetAmount(goal.targetAmount().amount());
        entity.setCurrentSaved(goal.currentSaved().amount());
        entity.setCurrency(goal.targetAmount().currency());
        entity.setTargetDate(goal.targetDate());
        entity.setCreatedAt(goal.createdAt());
        entity.setUpdatedAt(goal.updatedAt());
        return entity;
    }

    private static SavingsGoal toDomain(SavingsGoalEntity entity) {
        return new SavingsGoal(
                entity.getId(), entity.getProfileId(), entity.getGoalName(), entity.getGoalType(),
                new Money(entity.getTargetAmount(), entity.getCurrency()),
                new Money(entity.getCurrentSaved(), entity.getCurrency()),
                entity.getTargetDate(), entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }
}
