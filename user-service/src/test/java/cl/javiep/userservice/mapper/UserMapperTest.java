package cl.javiep.userservice.mapper;

import cl.javiep.userservice.dto.UserRequestDTO;
import cl.javiep.userservice.dto.UserResponseDTO;
import cl.javiep.userservice.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void toEntity_shouldMapAllFields_whenDtoIsValid() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("Juan Pérez");
        dto.setEmail("juan@example.com");
        dto.setBio("Lector ávido");

        User user = mapper.toEntity(dto);

        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isEqualTo("Juan Pérez");
        assertThat(user.getEmail()).isEqualTo("juan@example.com");
        assertThat(user.getBio()).isEqualTo("Lector ávido");
        assertThat(user.getPasswordHash()).isNull();
        assertThat(user.getCreatedAt()).isNull();
    }

    @Test
    void toResponseDTO_shouldMapAllFields_whenUserIsValid() {
        User user = new User();
        user.setId(1L);
        user.setName("María García");
        user.setEmail("maria@example.com");
        user.setBio("Amante de los libros");
        user.setPasswordHash("hash_ignorado");
        user.setCreatedAt(LocalDateTime.of(2026, 1, 15, 10, 30));

        UserResponseDTO dto = mapper.toResponseDTO(user);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("María García");
        assertThat(dto.getEmail()).isEqualTo("maria@example.com");
        assertThat(dto.getBio()).isEqualTo("Amante de los libros");
        assertThat(dto.getCreatedAt()).isEqualTo("2026-01-15T10:30:00");
    }
}
