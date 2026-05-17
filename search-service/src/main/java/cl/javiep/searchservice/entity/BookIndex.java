package cl.javiep.searchservice.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_index")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private String title;

    private String author;
    private String genre;
    private String tags;
    private Double rating;
}
