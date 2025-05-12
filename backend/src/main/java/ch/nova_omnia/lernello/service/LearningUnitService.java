package ch.nova_omnia.lernello.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.dto.request.UpdateLearningUnitOrderDTO;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.progress.LearningUnitProgress;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.LearningUnitProgressRepository;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearningUnitService {
    private final LearningUnitRepository learningUnitRepository;
    private final LearningKitRepository learningKitRepository;
    private final LearningUnitProgressRepository learningUnitProgressRepository;
    private final AIBlockService aiBlockService;

    @Transactional
    public LearningUnit createLearningUnit(LearningUnit learningUnit, UUID learningKitId) {
        LearningKit learningKit = learningKitRepository.findById(learningKitId).orElseThrow(() -> new RuntimeException("LearningKit not found with ID: " + learningKitId));
        learningUnit.setPosition(learningKit.getLearningUnits().size());
        return learningUnitRepository.save(learningUnit);
    }

    public Optional<LearningUnit> findById(UUID id) {
        return learningUnitRepository.findById(id);
    }

    public List<LearningUnit> findAll() {
        return learningUnitRepository.findAll();
    }

    @Transactional
    public void updateLearningUnitPosition(UUID learningKitId, UpdateLearningUnitOrderDTO updateLearningUnitOrderDTO) {
        List<UUID> learningUnitIds = updateLearningUnitOrderDTO.learningUnitUuidsInOrder();
        List<LearningUnit> learningUnits = learningUnitRepository.findLearningUnitAsc(learningKitId);

        for (int i = 0; i < learningUnitIds.size(); i++) {
            UUID expectedId = learningUnitIds.get(i);
            LearningUnit learningUnit = learningUnits.stream().filter(lu -> lu.getUuid().equals(expectedId)).findFirst().orElseThrow(() -> new RuntimeException("Learning Unit not found: " + expectedId));

            learningUnitRepository.updatePosition(i, learningUnit.getUuid());
        }
    }

    @Transactional
    public void deleteById(UUID id) {
        List<LearningUnitProgress> progressesToDelete = learningUnitProgressRepository.findAllByLearningUnit_Uuid(id);
        learningUnitProgressRepository.deleteAll(progressesToDelete);
        learningUnitRepository.deleteById(id);
    }

    @Transactional
    public LearningUnit generateLearningUnitWithAI(List<UUID> fileIds, UUID learningUnitId) {
        LearningUnit learningUnit = getLearningUnit(learningUnitId);

        learningUnit.getBlocks().clear();

        List<Block> blocks = aiBlockService.generateBlocksAI(fileIds);

        for (Block block : blocks) {
            block.setLearningUnit(learningUnit);
            learningUnit.getBlocks().add(block);
        }

        return learningUnitRepository.save(learningUnit);
    }

    private LearningUnit getLearningUnit(UUID id) {
        Optional<LearningUnit> optionalLearningUnit = findById(id);
        if (optionalLearningUnit.isEmpty()) {
            throw new IllegalArgumentException("Learning unit with id " + id + " not found");
        }
        return optionalLearningUnit.get();
    }
}
