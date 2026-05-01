package cl.javiep.readinglistservice.dto;

import cl.javiep.readinglistservice.model.ListType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ListRequestDTO {

    @NotNull(message = "El userId es obligatorio")
    private Long userId;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotNull(message = "El tipo es obligatorio")
    private ListType type;  // debe ser: WANT_TO_READ, READING o READ

    private Boolean isPrivate = false;
}