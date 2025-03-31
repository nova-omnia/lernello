package ch.nova_omnia.lernello.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearningUnitService {
    private final LearningUnitRepository learningUnitRepository;

    @Transactional
    public LearningUnit createLearningUnit(LearningUnit learningUnit) {
        return learningUnitRepository.save(learningUnit);
    }

    public Optional<LearningUnit> findById(UUID id) {
        return learningUnitRepository.findById(id);
    }

    @Transactional
    public Optional<LearningUnit> updateBlockOrder(UUID id, List<UUID> blockIds) {
        Optional<LearningUnit> opt = findById(id);
        if (opt.isEmpty()) {
            return Optional.empty();
        }
        LearningUnit unit = opt.get();
        learningUnitRepository.save(unit);
        return Optional.of(unit);
    }

    public List<LearningUnit> findAll() {
        return learningUnitRepository.findAll();
    }

    public void deleteById(UUID id) {
        learningUnitRepository.deleteById(id);
    }
}