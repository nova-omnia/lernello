package ch.nova_omnia.lernello.repository;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.progress.LearningKitProgress;
import ch.nova_omnia.lernello.model.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LearningKitProgressRepository extends JpaRepository<LearningKitProgress, UUID> {
    Optional<LearningKitProgress> findByUserAndLearningKit(User user, LearningKit learningKit);
    Optional<LearningKitProgress> findByUser_UuidAndLearningKit_Uuid(UUID userId, UUID learningKitId);
}
