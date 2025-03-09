package ch.nova_omnia.pm4.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ch.nova_omnia.pm4.model.data.Instructor;

/**
 * InstructorRepository is a repository interface for managing {@link Instructor} entities.
 * It extends {@link CrudRepository} to provide CRUD operations.
 */
@Repository
public interface InstructorRepository extends CrudRepository<Instructor, Long> {
}