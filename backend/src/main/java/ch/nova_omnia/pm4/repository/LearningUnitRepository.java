package ch.nova_omnia.pm4.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ch.nova_omnia.pm4.model.data.LearningUnit;

/**
 * LearningUnitRepository is a repository interface for managing {@link LearningUnit} entities.
 * It extends {@link CrudRepository} to provide CRUD operations.
 */
@Repository
public interface LearningUnitRepository extends CrudRepository<LearningUnit, Long> {
}