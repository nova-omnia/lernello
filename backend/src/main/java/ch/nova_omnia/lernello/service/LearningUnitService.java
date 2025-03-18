package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class LearningUnitService {

    private final LearningUnitRepository learningUnitRepository;
    private final LearningKitRepository learningKitRepository;

    public LearningUnitService(LearningUnitRepository learningUnitRepository,
                               LearningKitRepository learningKitRepository) {
        this.learningUnitRepository = learningUnitRepository;
        this.learningKitRepository = learningKitRepository;
    }

    @Transactional
    public LearningUnit createLearningUnit(String name) {
        LearningKit activeKit = learningKitRepository.getReferenceById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        LearningUnit learningUnit = new LearningUnit(name, activeKit);
        return learningUnitRepository.save(learningUnit);
    }

    public Optional<LearningUnit> findById(UUID id) {
        return learningUnitRepository.findById(id);
    }

    public void save(LearningUnit unit) {
        learningUnitRepository.save(unit);
    }
}