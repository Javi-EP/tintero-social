package cl.javiep.searchservice.repository;

import cl.javiep.searchservice.entity.UserIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserIndexRepository extends JpaRepository<UserIndex, Long> {

    List<UserIndex> findByNameContainingIgnoreCase(String name);
    Optional<UserIndex> findByUserId(Long userId);
}