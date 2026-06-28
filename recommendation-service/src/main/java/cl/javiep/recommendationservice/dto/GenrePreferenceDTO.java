package cl.javiep.recommendationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Datos para registrar una preferencia de género de un usuario")
public class GenrePreferenceDTO {

    @NotNull
    @Schema(description = "ID del usuario", example = "1")
    private Long userId;

    @NotNull
    @Schema(description = "ID del género preferido", example = "3")
    private Long genreId;

    @DecimalMin("0.0") @DecimalMax("1.0")
    @Schema(description = "Peso o nivel de preferencia hacia el género (mayor valor = más preferido)", example = "0.8")
    private Double weight;
}
