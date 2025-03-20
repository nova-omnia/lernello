package ch.nova_omnia.lernello.model.data;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {
    public enum Role {
        INSTRUCTOR, TRAINEE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "username", nullable = false, unique = true)
    @NotNull
    @NonNull
    @Email(message = "Invalid username format")
    private String username;

    @Column(name = "password", nullable = false)
    @NotNull
    @NonNull
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Column(name = "language", nullable = false)
    @NotNull
    @NonNull
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @NotNull(message = "Role is required")
    @NonNull
    private Role role;

    @Column(name = "create_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;
}
