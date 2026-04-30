package cl.javiep.userservice.controller;

import cl.javiep.userservice.dto.*;
import cl.javiep.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST /api/users/register — registrar nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(dto));
    }

    // POST /api/users/login — iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(userService.login(dto));
    }

    // GET /api/users/validate?token=xxx — valida un token JWT
    // Este endpoint lo usan los otros microservicios
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Boolean>> validate(@RequestParam String token) {
        boolean valid = userService.validateToken(token);
        return ResponseEntity.ok(Map.of("valid", valid));
    }

    // GET /api/users — listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    // GET /api/users/{id} — obtener perfil por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    // PUT /api/users/{id} — actualizar perfil
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    // DELETE /api/users/{id} — eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}