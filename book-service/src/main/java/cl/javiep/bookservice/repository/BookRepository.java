package cl.javiep.bookservice.repository;

import cl.javiep.bookservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Al extender JpaRepository, Spring genera automáticamente:
// findAll(), findById(), save(), deleteById(), count(), etc.
// No necesitas escribir ningún SQL básico.
@Repository
public interface BookRepository extends JpaRepository<Book,Long>{
    // Spring interpreta el nombre del metodo y genera la query sola
    // Busca libros donde el campo "title" contenga este texto, sin importar mayusculas
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Busca libros donde el campo "author" contenga este texto, sin importar mayusculas
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // Busca libros donde el campo "genre" sea exactamente este valor
    List<Book> findByGenre(String genre);

    // Busca un libro por su ISBN (al poder no existir, usamos Optional)
    Optional<Book> findByIsbn(String isbn);

}
