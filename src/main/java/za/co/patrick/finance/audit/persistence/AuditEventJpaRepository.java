package za.co.patrick.finance.audit.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface AuditEventJpaRepository extends JpaRepository<AuditEventEntity, UUID> {
    List<AuditEventEntity> findByProfileIdOrderByOccurredAtDesc(UUID profileId);
}
