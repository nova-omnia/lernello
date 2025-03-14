package ch.nova_omnia.pm4.model.auth.user;

import ch.nova_omnia.pm4.model.auth.role.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRole {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public UserRole() {}

    // Getter & Setter
}
