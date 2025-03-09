package ch.nova_omnia.pm4.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ch.nova_omnia.pm4.model.data.LearningUnit;

@Repository
public interface LearningUnitRepository extends CrudRepository<LearningUnit, Long> {
}