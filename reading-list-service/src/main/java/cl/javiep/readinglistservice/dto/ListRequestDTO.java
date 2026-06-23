package cl.javiep.readinglistservice.dto;

import cl.javiep.readinglistservice.model.ListType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Datos para crear una lista de lectura")
public class ListRequestDTO {

    @Schema(description = "ID del usuario propietario", example = "1")
    @NotNull(message = "El userId es obligatorio")
    private Long userId;

    @Schema(description = "Nombre de la lista", example = "Favoritos 2026")
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Schema(description = "Tipo de lista: WANT_TO_READ, READING o READ", example = "WANT_TO_READ")
    @NotNull(message = "El tipo es obligatorio")
    private ListType type;

    @Schema(description = "Indica si la lista es privada", example = "false")
    private Boolean isPrivate = false;
}