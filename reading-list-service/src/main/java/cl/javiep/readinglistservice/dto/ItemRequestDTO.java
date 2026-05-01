package cl.javiep.readinglistservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemRequestDTO {

    @NotNull(message = "El bookId es obligatorio")
    private Long bookId;

    @Min(value = 0, message = "El progreso mínimo es 0")
    @Max(value = 100, message = "El progreso máximo es 100")
    private Integer progress = 0;
}