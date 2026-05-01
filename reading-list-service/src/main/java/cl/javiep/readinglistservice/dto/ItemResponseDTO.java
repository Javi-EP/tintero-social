package cl.javiep.readinglistservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ItemResponseDTO {

    private Long id;
    private Long bookId;
    private Integer progress;
    private LocalDateTime addedAt;
    private LocalDateTime finishedAt;
}