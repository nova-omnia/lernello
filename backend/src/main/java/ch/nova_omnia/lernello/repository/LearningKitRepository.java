package ch.nova_omnia.lernello.repository;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.nova_omnia.lernello.model.data.LearningKit;

@Repository
public interface LearningKitRepository extends JpaRepository<LearningKit, UUID> {
    Page<LearningKit> findAllByParticipantsUuid(
        @NotNull UUID participants_uuid, Pageable pageable
    );}
