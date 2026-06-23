package cl.javiep.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Credenciales de inicio de sesion")
public class LoginRequestDTO {
    @Schema(description = "Email del usuario", example = "eloy@mail.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @Schema(description = "Contrasena del usuario", example = "24%&srf$hsA23")
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
