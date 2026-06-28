package cl.javiep.recommendationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Datos de una recomendación de libro")
public class RecommendationResponseDTO {

    @Schema(description = "ID de la recomendación", example = "1")
    private Long id;

    @Schema(description = "ID del usuario al que se dirige la recomendación", example = "1")
    private Long userId;

    @Schema(description = "ID del libro recomendado", example = "10")
    private Long bookId;

    @Schema(description = "Puntuación de relevancia de la recomendación", example = "0.92")
    private Double score;

    @Schema(description = "Motivo de la recomendación", example = "Basado en tu interés por el género fantasía")
    private String reason;

    @Schema(description = "Indica si la recomendación fue descartada por el usuario", example = "false")
    private Boolean dismissed;

    @Schema(description = "Fecha y hora en que se generó la recomendación")
    private LocalDateTime createdAt;
}