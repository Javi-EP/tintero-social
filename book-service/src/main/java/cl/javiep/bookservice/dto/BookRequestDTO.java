package cl.javiep.bookservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Lo que el cliente envía al crear o actualizar un libro
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestDTO {
    // Las validaciones van acá, NO en la entidad
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no puede superar 255 caracteres")
    private String title;

    @NotBlank(message = "El autor es obligatorio")
    private String author;

    private String isbn;

    private String genre;

    @Size(max = 2000, message = "La sinopsis no puede superar 2000 caracteres")
    private String synopsis;

    @Max(value = 2026, message = "El año no parece válido")
    private Integer publicationYear;
}
