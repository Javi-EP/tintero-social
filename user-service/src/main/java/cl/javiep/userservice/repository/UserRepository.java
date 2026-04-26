package cl.javiep.userservice.repository;

import cl.javiep.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    // Busca por email — usado en login y para verificar duplicados
    Optional<User> findByEmail(String email);

    // Verifica si ya existe un usuario con ese email
    boolean existsByEmail(String email);
}
