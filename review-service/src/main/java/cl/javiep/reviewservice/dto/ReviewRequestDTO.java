package cl.javiep.reviewservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Datos para crear o editar una reseña")
public class ReviewRequestDTO {

    @NotNull
    @Schema(description = "ID del usuario que escribe la reseña", example = "1")
    private Long userId;

    @NotNull
    @Schema(description = "ID del libro reseñado", example = "10")
    private Long bookId;

    @NotNull @Min(1) @Max(5)
    @Schema(description = "Calificación del libro del 1 al 5", example = "4")
    private Integer rating;

    @NotBlank
    @Schema(description = "Título corto de la reseña", example = "Muy buena lectura")
    private String title;

    @NotBlank
    @Schema(description = "Contenido detallado de la reseña", example = "Me encantó el desarrollo de personajes.")
    private String content;
}