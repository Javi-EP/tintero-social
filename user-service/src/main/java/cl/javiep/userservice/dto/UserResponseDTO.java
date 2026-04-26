package cl.javiep.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// lo que devuelve la API, sin passwordHash
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private String avatarUrl;
    private LocalDateTime createdAt;
    // passwordHash nunca aparece acá
}
