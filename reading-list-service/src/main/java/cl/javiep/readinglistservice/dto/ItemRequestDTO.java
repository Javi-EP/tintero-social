package cl.javiep.readinglistservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Datos para agregar un libro a una lista")
public class ItemRequestDTO {

    @Schema(description = "ID del libro", example = "10")
    @NotNull(message = "El bookId es obligatorio")
    private Long bookId;

    @Schema(description = "Progreso de lectura 0-100", example = "50")
    @Min(value = 0, message = "El progreso mínimo es 0")
    @Max(value = 100, message = "El progreso máximo es 100")
    private Integer progress = 0;
}