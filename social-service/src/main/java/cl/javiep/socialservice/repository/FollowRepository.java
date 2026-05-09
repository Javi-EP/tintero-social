package cl.javiep.socialservice.repository;

import cl.javiep.socialservice.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    //Metodo para ver a todos los que sigue un usuario
    List<Follow> findByFollowerId(Long followerId);

    //Metodo para ver todos los seguidores de un usuario
    List<Follow> findByFollowedId(Long followedId);

    //Metodo para verificar si ya existe el follow
    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);

    Optional<Follow> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    //Metodo para contar cantidad de seguidos
    long countByFollowerId(Long followerId);

    //Metodo para contar cuantos seguidores tiene un usuario
    long countByFollowedId(Long followedId);
}
