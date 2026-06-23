package cl.javiep.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta de inicio de sesion con token JWT")
public class LoginResponseDTO {
    @Schema(description = "Token JWT de autenticacion", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Tipo de token", example = "Bearer")
    private String type;

    @Schema(description = "ID del usuario autenticado", example = "1")
    private Long userId;

    @Schema(description = "Nombre del usuario", example = "Eloy Contreras")
    private String name;

    @Schema(description = "Email del usuario", example = "eloy@mail.com")
    private String email;
}
