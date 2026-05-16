package cl.javiep.recommendationservice.repository;

import cl.javiep.recommendationservice.entity.GenrePreference;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GenrePreferenceRepository extends JpaRepository<GenrePreference, Long> {

    List<GenrePreference> findByUserId(Long userId);
    boolean existsByUserIdAndGenreId(Long userId, Long genreId);
}

