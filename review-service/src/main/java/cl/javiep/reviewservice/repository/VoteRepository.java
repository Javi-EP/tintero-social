package cl.javiep.reviewservice.repository;

import cl.javiep.reviewservice.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean existsByUserIdAndReviewId(Long userId, Long reviewId);
}
