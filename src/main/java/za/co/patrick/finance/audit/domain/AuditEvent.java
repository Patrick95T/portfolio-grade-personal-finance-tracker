package za.co.patrick.finance.audit.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import za.co.patrick.finance.audit.application.AuditEventType;

public record AuditEvent(
        UUID id,
        UUID profileId,
        AuditEventType eventType,
        String entityType,
        UUID entityId,
        String summary,
        Instant occurredAt
) {
    public AuditEvent {
        Objects.requireNonNull(id, "id is required");
        Objects.requireNonNull(profileId, "profileId is required");
        Objects.requireNonNull(eventType, "eventType is required");
        Objects.requireNonNull(entityType, "entityType is required");
        Objects.requireNonNull(entityId, "entityId is required");
        Objects.requireNonNull(summary, "summary is required");
        Objects.requireNonNull(occurredAt, "occurredAt is required");
    }
}
