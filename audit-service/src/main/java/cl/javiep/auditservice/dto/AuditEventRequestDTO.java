package cl.javiep.auditservice.dto;

import cl.javiep.auditservice.model.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Datos para registrar un nuevo evento de auditoría")
public class AuditEventRequestDTO {

    @Schema(description = "ID del usuario que generó el evento", example = "1")
    private Long userId;

    @Schema(description = "Tipo de evento de auditoría", example = "LOGIN")
    @NotNull(message = "Event type is required")
    private EventType eventType;

    @Schema(description = "Descripción detallada del evento", example = "Usuario inició sesión exitosamente")
    @NotBlank(message = "Description is required")
    private String description;

    @Schema(description = "ID del recurso afectado por el evento", example = "10")
    private Long resourceId;

    @Schema(description = "Tipo de recurso afectado", example = "BOOK")
    private String resourceType;
}