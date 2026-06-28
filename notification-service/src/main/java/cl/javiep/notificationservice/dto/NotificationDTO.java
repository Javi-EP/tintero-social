package cl.javiep.notificationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de una notificación interna")
public class NotificationDTO {

    @Schema(description = "ID de la notificación", example = "1")
    private Long id;

    @Schema(description = "ID del usuario que recibe la notificación", example = "1")
    private Long userId;

    @Schema(description = "Tipo de notificación", example = "NEW_FOLLOWER")
    private String type;

    @Schema(description = "Mensaje de la notificación", example = "Tienes un nuevo seguidor")
    private String message;

    @Schema(description = "Indica si la notificación ya fue leída", example = "false")
    private boolean read;

    @Schema(description = "Fecha y hora en que se creó la notificación")
    private LocalDateTime createdAt;

    @Schema(description = "Nombre del usuario que originó la notificación", example = "Juan Pérez")
    private String senderName;
}