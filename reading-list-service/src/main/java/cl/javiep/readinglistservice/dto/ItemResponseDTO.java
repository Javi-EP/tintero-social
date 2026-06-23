package cl.javiep.readinglistservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Datos de un libro dentro de una lista de lectura")
public class ItemResponseDTO {

    @Schema(description = "ID del item", example = "1")
    private Long id;

    @Schema(description = "ID del libro", example = "10")
    private Long bookId;

    @Schema(description = "Progreso de lectura 0-100", example = "75")
    private Integer progress;

    @Schema(description = "Fecha en que se agrego el libro")
    private LocalDateTime addedAt;

    @Schema(description = "Fecha en que se termino el libro")
    private LocalDateTime finishedAt;
}