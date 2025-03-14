package ch.nova_omnia.pm4.model.auth.role;

import ch.nova_omnia.pm4.model.auth.permission.Permission;
import jakarta.persistence.*;

@Entity
@Table(name = "role_permissions")
public class RolePermission {

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Id
    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    public RolePermission() {}

    // Getter & Setter
}
