package cl.javiep.searchservice.repository;

import cl.javiep.searchservice.entity.BookIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookIndexRepository extends JpaRepository<BookIndex, Long> {

    List<BookIndex> findByTitleContainingIgnoreCase(String title);
    List<BookIndex> findByAuthorContainingIgnoreCase(String author);
    List<BookIndex> findByGenreContainingIgnoreCase(String genre);
    List<BookIndex> findByRatingGreaterThanEqual(Double rating);
    Optional<BookIndex> findByBookId(Long bookId);
}
