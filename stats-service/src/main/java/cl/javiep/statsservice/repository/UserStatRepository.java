package cl.javiep.statsservice.repository;

import cl.javiep.statsservice.model.UserStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStatRepository extends JpaRepository<UserStat,Long> {

    //Todas las estadisticas de un usuario
    List<UserStat> findByUserId(Long userId);

    //Libros leidos por usuario
    List<UserStat> findByUserIdAndBooksReadId(Long userId, Long booksReadId);

    //Paginas leidas por usuario
    List<UserStat> findByUserIdAndPagesRead(Long userId, Long pagesRead);

    //Genero favorito del usuario
    List<UserStat> findByUserIdAndFavGenre(Long userId, String favGenre);
}
