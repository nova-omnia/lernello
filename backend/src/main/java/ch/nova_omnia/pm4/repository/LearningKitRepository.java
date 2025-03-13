package ch.nova_omnia.pm4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ch.nova_omnia.pm4.model.data.LearningKit;

@Repository
public interface LearningKitRepository extends JpaRepository<LearningKit, Long> {
}
