package cl.javiep.recommendationservice.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecommendationRequestDTO {
    @NotNull
    private Long userId;

    @NotNull
    private Long bookId;

    @DecimalMin("0.0") @DecimalMax("1.0")
    private Double score;

    @NotBlank
    private String reason;
}