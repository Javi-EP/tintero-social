package cl.javiep.bookservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de un libro")
public class BookResponseDTO {
    @Schema(description = "ID del libro", example = "1")
    private Long id;

    @Schema(description = "Titulo del libro", example = "El Principito")
    private String title;

    @Schema(description = "Autor del libro", example = "Antoine de Saint-Exupery")
    private String author;

    @Schema(description = "ISBN del libro", example = "978-3-16-148410-0")
    private String isbn;

    @Schema(description = "Genero literario", example = "Fantasia")
    private String genre;

    @Schema(description = "Sinopsis del libro", example = "Un piloto perdido en el desierto conoce a un pequeno principe...")
    private String synopsis;

    @Schema(description = "Anio de publicacion", example = "1943")
    private Integer publicationYear;

    @Schema(description = "Rating promedio del libro (proximamente)")
    private Double ratingPromedio;
}
