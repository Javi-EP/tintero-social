package cl.javiep.readinglistservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reading_list_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del libro (viene de book-service)
    @Column(nullable = false)
    private Long bookId;

    // Progreso de lectura en porcentaje (0-100)
    private Integer progress = 0;

    private LocalDateTime addedAt;
    private LocalDateTime finishedAt;

    // @ManyToOne — muchos items pertenecen a una lista
    // @JoinColumn define la columna de la llave foránea en la tabla
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reading_list_id", nullable = false)
    private ReadingList readingList;

    @PrePersist
    protected void onCreate() {
        this.addedAt = LocalDateTime.now();
    }
}