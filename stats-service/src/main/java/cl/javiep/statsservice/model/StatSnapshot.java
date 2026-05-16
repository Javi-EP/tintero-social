package cl.javiep.statsservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "stat_snapshots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private Integer booksRead = 0;

    @Column(nullable = false)
    private Integer booksReading = 0;

    @Column(nullable = false)
    private Integer booksWantToRead = 0;

    @Column(nullable = false)
    private Integer totalReviews = 0;

    @Column(nullable = true)
    private Double averageRating = 0.0;

    @Column(nullable = true, length = 100)
    private String favoriteGenre = "N/A";

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}