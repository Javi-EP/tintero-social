package cl.javiep.bookservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos para crear o actualizar un libro")
public class BookRequestDTO {
    @Schema(description = "Titulo del libro", example = "El Principito")
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no puede superar 255 caracteres")
    private String title;

    @Schema(description = "Autor del libro", example = "Antoine de Saint-Exupery")
    @NotBlank(message = "El autor es obligatorio")
    private String author;

    @Schema(description = "ISBN del libro", example = "978-3-16-148410-0")
    private String isbn;

    @Schema(description = "Genero literario", example = "Fantasia")
    private String genre;

    @Schema(description = "Sinopsis del libro", example = "Un piloto perdido en el desierto conoce a un pequeno principe...")
    @Size(max = 2000, message = "La sinopsis no puede superar 2000 caracteres")
    private String synopsis;

    @Schema(description = "Anio de publicacion", example = "1943")
    @Max(value = 2026, message = "El año no parece válido")
    private Integer publicationYear;
}
