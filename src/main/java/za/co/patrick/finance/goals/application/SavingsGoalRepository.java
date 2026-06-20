package za.co.patrick.finance.goals.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import za.co.patrick.finance.goals.domain.SavingsGoal;

public interface SavingsGoalRepository {
    SavingsGoal save(SavingsGoal savingsGoal);
    List<SavingsGoal> findByProfileId(UUID profileId);
    Optional<SavingsGoal> findById(UUID savingsGoalId);
}
