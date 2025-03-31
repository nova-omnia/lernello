package ch.nova_omnia.lernello.model.data;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    public enum Role {
        INSTRUCTOR, TRAINEE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    @NotNull
    private UUID uuid;

    @Column(name = "username", nullable = false, unique = true)
    @NotBlank
    @Email
    private String username;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 8)
    private String password;

    @Column(name = "changed_password", nullable = false)
    private boolean changedPassword;

    @Column(name = "language", nullable = false)
    @NotBlank
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @NotNull
    private Role role;

    @Column(name = "create_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @Transient
    private String token;

    @Transient
    private int expires;


    public User(String username, String password, String language, Role role) {
        this.username = username;
        this.password = password;
        this.language = language;
        this.role = role;
    }
}
