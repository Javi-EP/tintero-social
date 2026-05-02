package cl.javiep.readinglistservice.repository;

import cl.javiep.readinglistservice.model.ReadingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReadingListItemRepository extends JpaRepository<ReadingListItem, Long> {

    // Verificar si un libro ya está en una lista específica
    boolean existsByReadingListIdAndBookId(Long readingListId, Long bookId);

    // Buscar un item específico dentro de una lista
    Optional<ReadingListItem> findByReadingListIdAndBookId(Long readingListId, Long bookId);
}