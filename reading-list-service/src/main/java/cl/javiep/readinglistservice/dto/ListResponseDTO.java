package cl.javiep.readinglistservice.dto;

import cl.javiep.readinglistservice.model.ListType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Datos de una lista de lectura")
public class ListResponseDTO {

    @Schema(description = "ID de la lista", example = "1")
    private Long id;

    @Schema(description = "ID del usuario propietario", example = "1")
    private Long userId;

    @Schema(description = "Nombre de la lista", example = "Favoritos 2026")
    private String name;

    @Schema(description = "Tipo de lista: WANT_TO_READ, READING o READ")
    private ListType type;

    @Schema(description = "Indica si la lista es privada", example = "false")
    private Boolean isPrivate;

    @Schema(description = "Fecha de creacion")
    private LocalDateTime createdAt;

    @Schema(description = "Libros en la lista")
    private List<ItemResponseDTO> items;
}