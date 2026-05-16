package cl.javiep.recommendationservice.repository;

import cl.javiep.recommendationservice.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByUserIdAndDismissedFalse(Long userId);
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
}
