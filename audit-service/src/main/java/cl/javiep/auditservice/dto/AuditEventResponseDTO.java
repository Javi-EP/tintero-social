package cl.javiep.auditservice.dto;

import cl.javiep.auditservice.model.EventType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditEventResponseDTO {

    private Long id;
    private Long userId;
    private EventType eventType;
    private String description;
    private Long resourceId;
    private String resourceType;
    private LocalDateTime createdAt;
}