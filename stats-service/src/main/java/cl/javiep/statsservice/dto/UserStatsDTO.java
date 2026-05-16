package cl.javiep.statsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsDTO {
    private Long userId;
    private Integer booksRead;
    private Integer booksReading;
    private Integer booksWantToRead;
    private Integer totalReviews;
    private Double averageRating;
    private String favoriteGenre;
    private LocalDateTime lastUpdated;
}