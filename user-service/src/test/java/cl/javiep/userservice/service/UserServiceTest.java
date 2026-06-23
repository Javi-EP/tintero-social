package cl.javiep.userservice.service;

import cl.javiep.userservice.dto.LoginRequestDTO;
import cl.javiep.userservice.dto.LoginResponseDTO;
import cl.javiep.userservice.dto.UserRequestDTO;
import cl.javiep.userservice.dto.UserResponseDTO;
import cl.javiep.userservice.mapper.UserMapper;
import cl.javiep.userservice.model.User;
import cl.javiep.userservice.repository.UserRepository;
import cl.javiep.userservice.security.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    // --- register() tests ---

    @Test
    void register_shouldSaveUser_whenEmailIsNotTaken() {
        UserRequestDTO dto = givenUserRequestDTO();
        User entity = givenUserEntity(null);
        User savedEntity = givenUserEntity(1L);
        UserResponseDTO expectedDto = givenUserResponseDTO();

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(entity);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encoded_password");
        when(userRepository.save(entity)).thenReturn(savedEntity);
        when(userMapper.toResponseDTO(savedEntity)).thenReturn(expectedDto);

        UserResponseDTO result = userService.register(dto);

        assertThat(result).isEqualTo(expectedDto);
        verify(passwordEncoder).encode(dto.getPassword());
        verify(userRepository).save(entity);
    }

    @Test
    void register_shouldThrow_whenEmailIsAlreadyTaken() {
        UserRequestDTO dto = givenUserRequestDTO();

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email ya está registrado");

        verify(userRepository, never()).save(any());
    }

    // --- login() tests ---

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        LoginRequestDTO dto = givenLoginRequestDTO();
        User user = givenUserEntity(1L);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())).thenReturn(true);
        when(jwtUtil.generateToken(user.getId(), user.getEmail())).thenReturn("jwt_token");

        LoginResponseDTO result = userService.login(dto);

        assertThat(result.getToken()).isEqualTo("jwt_token");
        assertThat(result.getType()).isEqualTo("Bearer");
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void login_shouldThrow_whenEmailNotFound() {
        LoginRequestDTO dto = givenLoginRequestDTO();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login(dto))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Credenciales inválidas");
    }

    @Test
    void login_shouldThrow_whenPasswordIsWrong() {
        LoginRequestDTO dto = givenLoginRequestDTO();
        User user = givenUserEntity(1L);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())).thenReturn(false);

        assertThatThrownBy(() -> userService.login(dto))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Credenciales inválidas");
    }

    // --- findById() tests ---

    @Test
    void findById_shouldReturnUser_whenExists() {
        User user = givenUserEntity(1L);
        UserResponseDTO expectedDto = givenUserResponseDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(expectedDto);

        UserResponseDTO result = userService.findById(1L);

        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    // --- findAll() tests ---

    @Test
    void findAll_shouldReturnList_whenUsersExist() {
        User user1 = givenUserEntity(1L);
        User user2 = givenUserEntity(2L);
        UserResponseDTO dto1 = givenUserResponseDTO();
        UserResponseDTO dto2 = givenUserResponseDTO();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.toResponseDTO(user1)).thenReturn(dto1);
        when(userMapper.toResponseDTO(user2)).thenReturn(dto2);

        List<UserResponseDTO> result = userService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(dto1, dto2);
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserResponseDTO> result = userService.findAll();

        assertThat(result).isEmpty();
    }

    // --- update() tests ---

    @Test
    void update_shouldUpdateNameAndBio_whenUserExists() {
        User existingUser = givenUserEntity(1L);
        existingUser.setName("Original");
        existingUser.setBio("Bio original");
        existingUser.setEmail("original@example.com");

        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("Actualizado");
        dto.setEmail("nuevo@example.com");
        dto.setPassword("password123");
        dto.setBio("Bio actualizada");

        User savedUser = givenUserEntity(1L);
        savedUser.setName("Actualizado");
        savedUser.setBio("Bio actualizada");
        savedUser.setEmail("original@example.com"); // email no debe cambiar

        UserResponseDTO expectedDto = givenUserResponseDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toResponseDTO(savedUser)).thenReturn(expectedDto);

        UserResponseDTO result = userService.update(1L, dto);

        assertThat(result).isEqualTo(expectedDto);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Actualizado");
        assertThat(captor.getValue().getBio()).isEqualTo("Bio actualizada");
        assertThat(captor.getValue().getEmail()).isEqualTo("original@example.com");
    }

    @Test
    void update_shouldThrow_whenUserNotFound() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("Test");

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(99L, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(userRepository, never()).save(any());
    }

    // --- delete() tests ---

    @Test
    void delete_shouldRemoveUser_whenExists() {
        User user = givenUserEntity(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrow_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(userRepository, never()).deleteById(any());
    }

    // --- validateToken() tests ---

    @Test
    void validateToken_shouldDelegateToJwtUtil() {
        when(jwtUtil.validateToken("some-token")).thenReturn(true);

        boolean result = userService.validateToken("some-token");

        assertThat(result).isTrue();
        verify(jwtUtil).validateToken("some-token");
    }

    // --- helper methods ---

    private static UserRequestDTO givenUserRequestDTO() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("Juan Pérez");
        dto.setEmail("juan@example.com");
        dto.setPassword("password123");
        dto.setBio("Lector ávido");
        return dto;
    }

    private static LoginRequestDTO givenLoginRequestDTO() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("juan@example.com");
        dto.setPassword("password123");
        return dto;
    }

    private static User givenUserEntity(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("Juan Pérez");
        user.setEmail("juan@example.com");
        user.setPasswordHash("encoded_hash");
        user.setBio("Lector ávido");
        user.setCreatedAt(LocalDateTime.of(2026, 1, 15, 10, 30));
        return user;
    }

    private static UserResponseDTO givenUserResponseDTO() {
        return new UserResponseDTO(
                1L, "Juan Pérez", "juan@example.com", "Lector ávido",
                LocalDateTime.of(2026, 1, 15, 10, 30)
        );
    }
}
