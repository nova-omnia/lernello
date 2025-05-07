package ch.nova_omnia.lernello.repository;

import ch.nova_omnia.lernello.model.data.LearningKit;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LearningKitRepository extends JpaRepository<LearningKit, UUID> {
    Page<LearningKit> findAllByParticipants_UuidAndPublishedTrue(UUID participantId,
                                                                 Pageable pageable);

    Page<LearningKit> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
