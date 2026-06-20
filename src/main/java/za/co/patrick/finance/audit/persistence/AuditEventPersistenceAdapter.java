package za.co.patrick.finance.audit.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import za.co.patrick.finance.audit.application.AuditEventRepository;
import za.co.patrick.finance.audit.domain.AuditEvent;

@Repository
public class AuditEventPersistenceAdapter implements AuditEventRepository {

    private final AuditEventJpaRepository auditEventJpaRepository;

    public AuditEventPersistenceAdapter(AuditEventJpaRepository auditEventJpaRepository) {
        this.auditEventJpaRepository = auditEventJpaRepository;
    }

    @Override
    public AuditEvent save(AuditEvent auditEvent) {
        return toDomain(auditEventJpaRepository.save(toEntity(auditEvent)));
    }

    @Override
    public List<AuditEvent> findByProfileId(UUID profileId) {
        return auditEventJpaRepository.findByProfileIdOrderByOccurredAtDesc(profileId).stream()
                .map(AuditEventPersistenceAdapter::toDomain)
                .toList();
    }

    private static AuditEventEntity toEntity(AuditEvent auditEvent) {
        var entity = new AuditEventEntity();
        entity.setId(auditEvent.id());
        entity.setProfileId(auditEvent.profileId());
        entity.setEventType(auditEvent.eventType());
        entity.setEntityType(auditEvent.entityType());
        entity.setEntityId(auditEvent.entityId());
        entity.setSummary(auditEvent.summary());
        entity.setOccurredAt(auditEvent.occurredAt());
        return entity;
    }

    private static AuditEvent toDomain(AuditEventEntity entity) {
        return new AuditEvent(
                entity.getId(),
                entity.getProfileId(),
                entity.getEventType(),
                entity.getEntityType(),
                entity.getEntityId(),
                entity.getSummary(),
                entity.getOccurredAt()
        );
    }
}
