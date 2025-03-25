package ch.nova_omnia.lernello.repository;

import ch.nova_omnia.lernello.model.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);

    User findByUuid(UUID uuid);
}
