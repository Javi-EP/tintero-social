package cl.javiep.readinglistservice.dto;

import cl.javiep.readinglistservice.model.ListType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ListResponseDTO {

    private Long id;
    private Long userId;
    private String name;
    private ListType type;
    private Boolean isPrivate;
    private LocalDateTime createdAt;
    private List<ItemResponseDTO> items;
}