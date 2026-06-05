package cl.javiep.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @Schema(description = "Nombre del usuario a registrar", example = "Eloy Contreras")
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Schema(description = "Email del usuario", example = "eloy@mail.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "24%&srf$hsA23")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;  // llega en texto plano, se hashea en el service

    @Schema(description = "Descripcion del usuario", example = "Amante de los libros de fantasía y el terror")
    private String bio;
}
