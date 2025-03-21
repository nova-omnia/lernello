package ch.nova_omnia.lernello.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.nova_omnia.lernello.model.data.LearningUnit;

public interface LearningUnitRepository extends JpaRepository<LearningUnit, UUID> {
}
