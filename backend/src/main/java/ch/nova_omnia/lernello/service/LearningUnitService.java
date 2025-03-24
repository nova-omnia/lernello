package ch.nova_omnia.lernello.service;

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
        //TOOD: Implement
        return null;
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
        // Reorder blocks as needed (use blockIds to rearrange)
        // Save changes via repository
        learningUnitRepository.save(unit);
        return Optional.of(unit);
    }
}