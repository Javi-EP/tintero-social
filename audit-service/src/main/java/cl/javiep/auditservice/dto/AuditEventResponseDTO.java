package cl.javiep.auditservice.dto;

import cl.javiep.auditservice.model.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Datos de un evento de auditoría")
public class AuditEventResponseDTO {

    @Schema(description = "ID del evento de auditoría", example = "1")
    private Long id;

    @Schema(description = "ID del usuario que generó el evento", example = "1")
    private Long userId;

    @Schema(description = "Tipo de evento de auditoría", example = "LOGIN")
    private EventType eventType;

    @Schema(description = "Descripción detallada del evento", example = "Usuario inició sesión exitosamente")
    private String description;

    @Schema(description = "ID del recurso afectado por el evento", example = "10")
    private Long resourceId;

    @Schema(description = "Tipo de recurso afectado", example = "BOOK")
    private String resourceType;

    @Schema(description = "Fecha y hora en que se registró el evento")
    private LocalDateTime createdAt;
}