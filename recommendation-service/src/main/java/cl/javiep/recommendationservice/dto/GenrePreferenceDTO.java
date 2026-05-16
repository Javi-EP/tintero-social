package cl.javiep.recommendationservice.dto;

import lombok.Data;

@Data
public class GenrePreferenceDTO {
    private Long userId;
    private Long genreId;
    private Double weight;
}
