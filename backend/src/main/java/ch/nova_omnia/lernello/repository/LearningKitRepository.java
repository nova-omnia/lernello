package ch.nova_omnia.lernello.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.nova_omnia.lernello.model.data.File;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.user.User;

@Repository
public interface LearningKitRepository extends JpaRepository<LearningKit, UUID> {
    Page<LearningKit> findAllByTrainees_UuidAndPublishedTrue(UUID traineeId,
                                                                 Pageable pageable);

    Page<LearningKit> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<LearningKit> findAllByTraineesContains(User user);

    List<LearningKit> findAllByFilesContains(File file);
}
