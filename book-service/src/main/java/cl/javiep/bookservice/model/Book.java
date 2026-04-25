package cl.javiep.bookservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Entity le dice a JPA que esta clase representa una tabla en la BD
@Entity
// @Table define el nombre de la tabla
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    // @Id indica que este campo es PK
    @Id
    // @GeneratedValue hace que el ID se genere automáticamente
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @NotBlank valida que el campo no llegue vacío desde el request
    @NotBlank(message = "El título es obligatorio")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "El autor es obligatorio")
    @Column(nullable = false)
    private String author;

    // El ISBN es único: no pueden existir dos libros con el mismo
    @Column(unique = true)
    private String isbn;

    private String genre;

    // @Column(length = 2000) permite textos más largos que el default (255)
    @Column(length = 2000)
    private String synopsis;

    private Integer publicationYear;

}
