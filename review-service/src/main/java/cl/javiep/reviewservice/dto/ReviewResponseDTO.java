package cl.javiep.reviewservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponseDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private Integer rating;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
