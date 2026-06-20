package za.co.patrick.finance.goals.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SavingsGoalJpaRepository extends JpaRepository<SavingsGoalEntity, UUID> {
    List<SavingsGoalEntity> findByProfileIdOrderByCreatedAtAsc(UUID profileId);
}
