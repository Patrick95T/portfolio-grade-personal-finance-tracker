package za.co.patrick.finance.audit.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.patrick.finance.audit.application.AuditEventService;

@RestController
@RequestMapping("/api/v1/profiles/{profileId}/audit-events")
@Tag(name = "Audit", description = "Inspect auditable financial create actions")
public class AuditEventController {

    private final AuditEventService auditEventService;

    public AuditEventController(AuditEventService auditEventService) {
        this.auditEventService = auditEventService;
    }

    @GetMapping
    @Operation(summary = "List audit events for a profile")
    public List<AuditEventResponse> listAuditEvents(@PathVariable UUID profileId) {
        return auditEventService.listProfileAuditEvents(profileId).stream()
                .map(event -> new AuditEventResponse(
                        event.id(),
                        event.profileId(),
                        event.eventType(),
                        event.entityType(),
                        event.entityId(),
                        event.summary(),
                        event.occurredAt()
                ))
                .toList();
    }
}
