package ch.nova_omnia.lernello.repository;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.progress.LearningUnitProgress;
import ch.nova_omnia.lernello.model.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LearningUnitProgressRepository extends JpaRepository<LearningUnitProgress, UUID> {
    Optional<LearningUnitProgress> findByUserAndLearningUnit(User user, LearningUnit learningUnit);
    Optional<LearningUnitProgress> findByUser_UuidAndLearningUnit_Uuid(UUID userId, UUID learningUnitId);
    List<LearningUnitProgress> findAllByUser_UuidAndLearningKitProgress_LearningKit_Uuid(UUID userId, UUID learningKitId);
}
