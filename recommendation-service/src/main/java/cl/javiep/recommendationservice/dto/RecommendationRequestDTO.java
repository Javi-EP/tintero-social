package cl.javiep.recommendationservice.dto;

import lombok.Data;

@Data
public class RecommendationRequestDTO {
    private Long userId;
    private Long bookId;
    private Double score;
    private String reason;
}