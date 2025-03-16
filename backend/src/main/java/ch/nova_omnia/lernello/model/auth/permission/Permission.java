package ch.nova_omnia.lernello.model.auth.permission;

import java.util.HashSet;
import java.util.Set;

import ch.nova_omnia.lernello.model.auth.role.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();

    public Permission() {
    }

    // Getter & Setter
}
