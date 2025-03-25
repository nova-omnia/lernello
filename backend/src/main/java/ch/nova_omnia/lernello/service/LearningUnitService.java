package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LearningUnitService {
    private final LearningUnitRepository learningUnitRepository;

    @Transactional
    public LearningUnit createLearningUnit(String name, UUID kitId) {
        // TODO: Implement
        return null;
    }

    public Optional<LearningUnit> findById(UUID id) {
        return learningUnitRepository.findById(id);
    }
}