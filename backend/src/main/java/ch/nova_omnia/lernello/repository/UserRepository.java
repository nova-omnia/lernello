package ch.nova_omnia.lernello.repository;

import ch.nova_omnia.lernello.model.data.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String username);
}