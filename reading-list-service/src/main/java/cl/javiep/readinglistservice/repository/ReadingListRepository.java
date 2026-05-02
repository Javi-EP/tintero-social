package cl.javiep.readinglistservice.repository;

import cl.javiep.readinglistservice.model.ReadingList;
import cl.javiep.readinglistservice.model.ListType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadingListRepository extends JpaRepository<ReadingList, Long> {

    // Todas las listas de un usuario
    List<ReadingList> findByUserId(Long userId);

    // Listas de un usuario filtradas por tipo
    List<ReadingList> findByUserIdAndType(Long userId, ListType type);

    // Verificar si ya existe una lista del mismo tipo para ese usuario
    boolean existsByUserIdAndType(Long userId, ListType type);
}