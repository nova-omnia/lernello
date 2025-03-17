package ch.nova_omnia.pm4.service;

import ch.nova_omnia.pm4.model.learningUnit.LearningUnit;
import ch.nova_omnia.pm4.repository.LearningUnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LearningUnitService {

    private final LearningUnitRepository learningUnitRepository;

    public LearningUnitService(LearningUnitRepository learningUnitRepository) {
        this.learningUnitRepository = learningUnitRepository;
    }

    @Transactional
    public LearningUnit createLearningUnit(String name) {
        LearningUnit learningUnit = new LearningUnit(name);
        return learningUnitRepository.save(learningUnit);
    }

    public Optional<LearningUnit> findById(Long id) {
        return learningUnitRepository.findById(id);
    }
}

