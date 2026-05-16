package cl.javiep.statsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GlobalStatsDTO {
    private Integer totalUsers;
    private Integer totalBooks;
    private Integer totalReviews;
    private Double averageRatingGlobal;
}