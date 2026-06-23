package cl.javiep.socialservice.dto;

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
@Schema(description = "Elemento del feed de actividad")
public class FeedItemDTO {
    @Schema(description = "Tipo de evento: review o follow", example = "review")
    private String type;

    @Schema(description = "ID del usuario que genero el evento", example = "1")
    private Long userId;

    @Schema(description = "Descripcion legible del evento", example = "Eloy Contreras resenio El Principito")
    private String description;

    @Schema(description = "Datos extra segun el tipo de evento")
    private Object data;

    @Schema(description = "Fecha y hora del evento")
    private LocalDateTime timestamp;
}
