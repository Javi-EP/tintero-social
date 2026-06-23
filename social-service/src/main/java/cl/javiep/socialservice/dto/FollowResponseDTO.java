package cl.javiep.socialservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Datos de una relacion de seguimiento")
public class FollowResponseDTO {
    @Schema(description = "ID del seguimiento", example = "1")
    private Long id;

    @Schema(description = "ID del seguidor", example = "1")
    private Long followerId;

    @Schema(description = "ID del usuario seguido", example = "2")
    private Long followedId;

    @Schema(description = "Fecha en que se empezo a seguir")
    private LocalDateTime createdAt;
}
