package cl.javiep.reviewservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Datos de una reseña")
public class ReviewResponseDTO {

    @Schema(description = "ID de la reseña", example = "1")
    private Long id;

    @Schema(description = "ID del usuario autor", example = "1")
    private Long userId;

    @Schema(description = "ID del libro reseñado", example = "10")
    private Long bookId;

    @Schema(description = "Calificación del 1 al 5", example = "4")
    private Integer rating;

    @Schema(description = "Título de la reseña", example = "Muy buena lectura")
    private String title;

    @Schema(description = "Contenido de la reseña", example = "Me encantó el desarrollo de personajes.")
    private String content;

    @Schema(description = "Fecha de creación")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de última edición")
    private LocalDateTime updatedAt;
}