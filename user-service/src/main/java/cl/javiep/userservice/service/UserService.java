package cl.javiep.userservice.service;

import cl.javiep.userservice.dto.*;
import cl.javiep.userservice.mapper.UserMapper;
import cl.javiep.userservice.model.User;
import cl.javiep.userservice.repository.UserRepository;
import cl.javiep.userservice.security.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, UserMapper userMapper,
                       PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Registrar nuevo usuario
    public UserResponseDTO register(UserRequestDTO dto) {
        log.info("Registrando usuario: '{}' ",dto.getName());
        // Verificar que el email no esté en uso
        if (userRepository.existsByEmail(dto.getEmail())) {
            log.warn("El email '{}' ya ha sido utilizado.",dto.getEmail());
            throw new IllegalArgumentException("El email ya está registrado: " + dto.getEmail());
        }

        User user = userMapper.toEntity(dto);
        // Hashear la contraseña antes de guardar — NUNCA texto plano
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        log.info("Usuario registrado exitosamente: '{}'",user.getName());
        return userMapper.toResponseDTO(userRepository.save(user));
    }

    // Login — retorna token JWT si las credenciales son correctas
    public LoginResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        // Compara la contraseña ingresada con el hash guardado
        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail());

        return new LoginResponseDTO(
                token,
                "Bearer",
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    // Valida un token JWT — usado por otros microservicios
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    // Obtener perfil por ID
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        return userMapper.toResponseDTO(user);
    }

    // Listar todos los usuarios
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Actualizar perfil por id
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        user.setName(dto.getName());
        user.setBio(dto.getBio());
        // el email no se actualiza para evitar conflictos con tokens activos

        return userMapper.toResponseDTO(userRepository.save(user));
    }

    // Eliminar usuario por id
    public void delete(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        userRepository.deleteById(id);
    }
}