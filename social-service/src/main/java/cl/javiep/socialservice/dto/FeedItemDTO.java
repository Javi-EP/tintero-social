package cl.javiep.socialservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedItemDTO {
    // Tipo de evento: review o follow
    private String type;
    // ID del usuario que generó el evento
    private Long userId;
    // Descripción legible del evento
    private String description;
    // Datos extra segun el tipo de evento
    private Object data;
    private LocalDateTime timestamp;
}
