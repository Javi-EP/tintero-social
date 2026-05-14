package cl.javiep.statsservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.processing.Generated;

@Entity
@Table(name = "users_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long booksRead;

    @Column(nullable = true)
    private Long pagesRead;

    @Column(nullable = true)
    private String favGenre;
}
