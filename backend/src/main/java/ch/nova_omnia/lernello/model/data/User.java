package ch.nova_omnia.lernello.model.data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "surname", nullable = false)
    @NotBlank
    private String surname;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 8)
    private String password;

    @Column(name = "changed_password", nullable = false)
    private boolean changedPassword;

    @Column(name = "locale")
    private String locale;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, updatable = false)
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
    private ZonedDateTime expires;

    public User(String username, String surname, String name, String password, String locale, Role role) {
        this.username = username;
        this.surname = surname;
        this.name = name;
        this.password = password;
        this.locale = locale;
        this.role = role;
    }
}
