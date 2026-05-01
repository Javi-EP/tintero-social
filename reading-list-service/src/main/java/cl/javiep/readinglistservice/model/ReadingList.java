package cl.javiep.readinglistservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reading_lists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del usuario dueño de la lista (viene de user-service)
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    // @Enumerated indica que guardamos el nombre del enum como String
    // sin esto Hibernate guarda 0, 1, 2 (el índice) en vez de "READING"
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListType type;

    private Boolean isPrivate = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // @OneToMany — una lista tiene muchos items
    // cascade = ALL → si borras la lista, se borran sus items automáticamente
    // orphanRemoval → si sacas un item de la lista, se borra de la BD
    @OneToMany(mappedBy = "readingList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReadingListItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}