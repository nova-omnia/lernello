package ch.nova_omnia.lernello.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.nova_omnia.lernello.model.data.User;
import ch.nova_omnia.lernello.model.data.User.Role;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);

    List<User> findAllByRole(Role trainee);
}
