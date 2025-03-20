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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uuid", nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "username", nullable = false, unique = true)
    @NotNull
    @Email(message = "Invalid username format")
    private String username;

    @Column(name = "password", nullable = false)
    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Column(name = "language", nullable = false)
    @NotNull
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @NotNull(message = "Role is required")
    private Role role;

    @Column(name = "create_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;

    public User(@NotNull @Email(message = "Invalid username format") String username,
                @NotNull @Size(min = 8, message = "Password must be at least 8 characters long") String password,
                @NotNull String language,
                @NotNull(message = "Role is required") Role role) {
        this.username = username;
        this.password = password;
        this.language = language;
        this.role = role;
    }
}
