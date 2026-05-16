package cl.javiep.recommendationservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "genre_preferences",
        uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "genreId"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenrePreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long genreId;

    private Double weight;
}
