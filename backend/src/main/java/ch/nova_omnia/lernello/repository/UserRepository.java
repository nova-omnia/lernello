package ch.nova_omnia.lernello.repository;

import ch.nova_omnia.lernello.model.data.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);

    User getUserByUsername(@NotNull @NonNull @Email(message = "Invalid username format") String username);
}
