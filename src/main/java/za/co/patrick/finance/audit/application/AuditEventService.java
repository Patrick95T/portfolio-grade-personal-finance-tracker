package za.co.patrick.finance.audit.application;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.patrick.finance.audit.domain.AuditEvent;

@Service
@Transactional
public class AuditEventService {

    private final AuditEventRepository auditEventRepository;
    private final Clock clock;

    public AuditEventService(
            AuditEventRepository auditEventRepository,
            Clock clock
    ) {
        this.auditEventRepository = auditEventRepository;
        this.clock = clock;
    }

    public void record(
            UUID profileId,
            AuditEventType eventType,
            String entityType,
            UUID entityId,
            String summary
    ) {
        auditEventRepository.save(new AuditEvent(
                UUID.randomUUID(),
                profileId,
                eventType,
                entityType,
                entityId,
                summary,
                Instant.now(clock)
        ));
    }

    @Transactional(readOnly = true)
    public List<AuditEvent> listProfileAuditEvents(UUID profileId) {
        return auditEventRepository.findByProfileId(profileId);
    }
}
