package cl.javiep.socialservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

// Espejo del ReviewResponseDTO del review-service
// Solo incluimos los campos que necesitamos para el feed
@Data
public class ReviewDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private Integer rating;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}