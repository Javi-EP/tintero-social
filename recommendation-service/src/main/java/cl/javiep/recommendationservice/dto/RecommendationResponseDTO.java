package cl.javiep.recommendationservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
public class RecommendationResponseDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private Double score;
    private String reason;
    private Boolean dismissed;
    private LocalDateTime createdAt;
}