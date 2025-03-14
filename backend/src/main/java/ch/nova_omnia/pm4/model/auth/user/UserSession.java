package ch.nova_omnia.pm4.model.users;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_session")
public class UserSession {

    @Id
    private String id = UUID.randomUUID().toString();

    @Column(name = "jwt_token", length = 512, nullable = false)
    private String jwtToken;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "expiration_data", nullable = false)
    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserSession() {
        this.createDate = LocalDateTime.now();
    }

    // Getter & Setter
}
