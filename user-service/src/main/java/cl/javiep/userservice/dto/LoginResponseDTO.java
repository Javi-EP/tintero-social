package cl.javiep.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;       // el token JWT
    private String type;        // siempre "Bearer"
    private Long userId;
    private String name;
    private String email;
}
