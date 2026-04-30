package cl.javiep.userservice.service;

import cl.javiep.userservice.dto.*;
import cl.javiep.userservice.mapper.UserMapper;
import cl.javiep.userservice.model.User;
import cl.javiep.userservice.repository.UserRepository;
import cl.javiep.userservice.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
        // Verificar que el email no esté en uso
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado: " + dto.getEmail());
        }

        User user = userMapper.toEntity(dto);
        // Hashear la contraseña antes de guardar — NUNCA texto plano
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        return userMapper.toResponseDTO(userRepository.save(user));
    }

    // Login — retorna token JWT si las credenciales son correctas
    public LoginResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        // Compara la contraseña ingresada con el hash guardado
        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Credenciales inválidas");
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
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return userMapper.toResponseDTO(user);
    }

    // Listar todos los usuarios
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Actualizar perfil
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        user.setName(dto.getName());
        user.setBio(dto.getBio());
        user.setAvatarUrl(dto.getAvatarUrl());
        // el email no se actualiza para evitar conflictos con tokens activos

        return userMapper.toResponseDTO(userRepository.save(user));
    }

    // Eliminar usuario
    public void delete(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        userRepository.deleteById(id);
    }
}