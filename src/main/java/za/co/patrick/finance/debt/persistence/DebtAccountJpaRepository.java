package za.co.patrick.finance.debt.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface DebtAccountJpaRepository extends JpaRepository<DebtAccountEntity, UUID> {
    List<DebtAccountEntity> findByProfileIdOrderByCreatedAtAsc(UUID profileId);
}
