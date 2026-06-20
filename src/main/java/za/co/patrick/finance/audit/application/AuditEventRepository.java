package za.co.patrick.finance.audit.application;

import java.util.List;
import java.util.UUID;
import za.co.patrick.finance.audit.domain.AuditEvent;

public interface AuditEventRepository {
    AuditEvent save(AuditEvent auditEvent);
    List<AuditEvent> findByProfileId(UUID profileId);
}
