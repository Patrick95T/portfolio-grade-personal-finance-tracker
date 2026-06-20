package za.co.patrick.finance.audit.api;

import java.time.Instant;
import java.util.UUID;
import za.co.patrick.finance.audit.application.AuditEventType;

public record AuditEventResponse(
        UUID id,
        UUID profileId,
        AuditEventType eventType,
        String entityType,
        UUID entityId,
        String summary,
        Instant occurredAt
) {
}
