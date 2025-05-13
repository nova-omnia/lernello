package ch.nova_omnia.lernello.repository;

import ch.nova_omnia.lernello.model.data.user.Role;
import ch.nova_omnia.lernello.model.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);

    List<User> findAllByRoleOrderBySurnameAscNameAsc(Role role);

    User findByUuid(UUID uuid);
}
