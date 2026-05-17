package cl.javiep.auditservice.dto;

import cl.javiep.auditservice.model.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuditEventRequestDTO {

    private Long userId;

    @NotNull(message = "Event type is required")
    private EventType eventType;

    @NotBlank(message = "Description is required")
    private String description;

    private Long resourceId;

    private String resourceType;
}