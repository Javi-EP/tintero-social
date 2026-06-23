package cl.javiep.userservice.controller;

import cl.javiep.userservice.dto.*;
import cl.javiep.userservice.service.UserLinkAssembler;
import cl.javiep.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Users", description = "Operaciones para gestionar usuarios")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserLinkAssembler linkAssembler;

    public UserController(UserService userService, UserLinkAssembler linkAssembler) {
        this.userService = userService;
        this.linkAssembler = linkAssembler;
    }

    @Operation(summary = "Registrar usuario", description = "Registra a un usuario. La respuesta incluye enlaces HATEOAS en _links")
    @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente")
    @PostMapping("/register")
    public ResponseEntity<EntityModel<UserResponseDTO>> register(@Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO user = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(linkAssembler.toModel(user));
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(userService.login(dto));
    }

    @Operation(summary = "Validar token JWT", description = "Verifica si un token JWT es válido (usado por otros microservicios)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token validado")
    })
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Boolean>> validate(@RequestParam String token) {
        boolean valid = userService.validateToken(token);
        return ResponseEntity.ok(Map.of("valid", valid));
    }

    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios registrados. La respuesta incluye enlaces HATEOAS en _links")
    @ApiResponse(responseCode = "200", description = "Usuarios obtenidos correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserResponseDTO>>> findAll() {
        List<EntityModel<UserResponseDTO>> users = userService.findAll().stream()
                .map(linkAssembler::toModel)
                .toList();
        return ResponseEntity.ok(linkAssembler.toCollectionModel(users));
    }

    @Operation(summary = "Buscar usuario por id", description = "Devuelve el usuario con enlaces HATEOAS en _links")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponseDTO>> findById(@PathVariable Long id) {
        UserResponseDTO user = userService.findById(id);
        return ResponseEntity.ok(linkAssembler.toModel(user));
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente. La respuesta incluye enlaces HATEOAS en _links")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO user = userService.update(id, dto);
        return ResponseEntity.ok(linkAssembler.toModel(user));
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
