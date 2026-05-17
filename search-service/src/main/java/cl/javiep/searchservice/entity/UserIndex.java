package cl.javiep.searchservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_index")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    private String bio;
}
