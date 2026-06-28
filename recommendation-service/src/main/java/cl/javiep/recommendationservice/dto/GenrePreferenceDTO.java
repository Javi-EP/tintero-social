package cl.javiep.recommendationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos para registrar una preferencia de género de un usuario")
public class GenrePreferenceDTO {

    @Schema(description = "ID del usuario", example = "1")
    private Long userId;

    @Schema(description = "ID del género preferido", example = "3")
    private Long genreId;

    @Schema(description = "Peso o nivel de preferencia hacia el género (mayor valor = más preferido)", example = "0.8")
    private Double weight;
}
