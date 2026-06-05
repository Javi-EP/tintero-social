package cl.javiep.reviewservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos para crear o editar una reseña")
public class ReviewRequestDTO {

    @Schema(description = "ID del usuario que escribe la reseña", example = "1")
    private Long userId;

    @Schema(description = "ID del libro reseñado", example = "10")
    private Long bookId;

    @Schema(description = "Calificación del libro del 1 al 5", example = "4")
    private Integer rating;

    @Schema(description = "Título corto de la reseña", example = "Muy buena lectura")
    private String title;

    @Schema(description = "Contenido detallado de la reseña", example = "Me encantó el desarrollo de personajes.")
    private String content;
}