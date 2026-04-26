package cl.javiep.bookservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Lo que el cliente recibe como respuesta
// Acá puedes agregar campos calculados o quitar campos sensibles
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO {
    private Long id;           // el cliente necesita el ID para futuras peticiones
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private String synopsis;
    private Integer publicationYear;

    // Campo calculado: en el futuro lo puedes poblar
    // consultando al review-service para obtener el promedio real
    private Double ratingPromedio;
}
