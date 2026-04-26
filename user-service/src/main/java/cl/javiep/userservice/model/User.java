package cl.javiep.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    // Nunca guardamos la contraseña en texto plano — siempre el hash
    @Column(nullable = false)
    private String passwordHash;

    private String bio;

    private String avatarUrl;

    // Se asigna automáticamente al crear el usuario
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // @PrePersist se ejecuta justo antes de guardar en la BD
    // para no tener que añadir la fecha de creacion de usuario manual
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
