package ch.nova_omnia.lernello.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.nova_omnia.lernello.dto.request.block.create.CreateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.create.CreateQuestionBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.create.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.dto.request.blockActions.AddBlockActionDTO;
import ch.nova_omnia.lernello.dto.request.blockActions.BlockActionDTO;
import ch.nova_omnia.lernello.dto.request.blockActions.RemoveBlockActionDTO;
import ch.nova_omnia.lernello.dto.request.blockActions.ReorderBlockActionDTO;
import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.QuestionBlock;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearningUnitService {
    private final LearningUnitRepository learningUnitRepository;
    private Map<String, UUID> temporaryKeyMap;

    @Transactional
    public LearningUnit createLearningUnit(LearningUnit learningUnit) {
        return learningUnitRepository.save(learningUnit);
    }

    public Optional<LearningUnit> findById(UUID id) {
        return learningUnitRepository.findById(id);
    }

    public List<LearningUnit> findAll() {
        return learningUnitRepository.findAll();
    }

    public void deleteById(UUID id) {
        learningUnitRepository.deleteById(id);
    }

    @Transactional
    public Map<String, UUID> applyLearningUnitActions(UUID id, List<BlockActionDTO> actions) throws IllegalArgumentException {

        actions = filterCorrelatedActions(actions);

        LearningUnit learningUnit = getLearningUnit(id);
        temporaryKeyMap = Map.of();

        for (BlockActionDTO action : actions) {
            switch (action) {
                case AddBlockActionDTO addAction -> addBlock(learningUnit, addAction);
                case RemoveBlockActionDTO removeAction-> removeBlock(learningUnit, removeAction);
                case ReorderBlockActionDTO reorderAction -> reorderBlocks(learningUnit, reorderAction);
                default -> throw new IllegalArgumentException("Unknown action type: " + action.getClass());
            }
        }
        return temporaryKeyMap;
    }

    private void addBlock(LearningUnit learningUnit, AddBlockActionDTO addAction) throws IllegalArgumentException {
        Block block = null;
        switch (addAction.type()) {

            case THEORY: {
                assert addAction.blockPayload() instanceof CreateTheoryBlockDTO;
                CreateTheoryBlockDTO theoryBlockDTO = (CreateTheoryBlockDTO) addAction.blockPayload();
                block = new TheoryBlock(
                        theoryBlockDTO.name(),
                        theoryBlockDTO.position(),
                        learningUnit,
                        theoryBlockDTO.content()
                );
                break;
            }
            case MULTIPLE_CHOICE: {
                assert addAction.blockPayload() instanceof CreateMultipleChoiceBlockDTO;
                CreateMultipleChoiceBlockDTO multipleChoiceBlockDTO = (CreateMultipleChoiceBlockDTO) addAction.blockPayload();
                block = new MultipleChoiceBlock(
                        multipleChoiceBlockDTO.name(),
                        multipleChoiceBlockDTO.position(),
                        learningUnit,
                        multipleChoiceBlockDTO.question(),
                        multipleChoiceBlockDTO.possibleAnswers(),
                        multipleChoiceBlockDTO.correctAnswers()
                );
                break;
            }
            case QUESTION: {
                assert addAction.blockPayload() instanceof CreateMultipleChoiceBlockDTO;
                CreateQuestionBlockDTO questionBlockDTO = (CreateQuestionBlockDTO) addAction.blockPayload();
                block = new QuestionBlock(
                        questionBlockDTO.name(),
                        questionBlockDTO.position(),
                        learningUnit,
                        questionBlockDTO.question(),
                        questionBlockDTO.expectedAnswer()
                );
                break;
            }
            default: throw new IllegalArgumentException("Unknown block type: " + addAction.type());
        }

        if (addAction.index() != null) {
            learningUnit.getBlocks().add(addAction.index(), block);
        } else {
            learningUnit.getBlocks().add(block);
        }

        if (addAction.blockId() != null) {
            temporaryKeyMap.put(addAction.blockId(), block.getUuid());
        }
    }

    private void removeBlock(LearningUnit learningUnit, RemoveBlockActionDTO removeAction) {


        if (learningUnit.getBlocks().stream().anyMatch(block -> block.getUuid().equals(removeAction.blockId()))) {
            learningUnit.getBlocks().removeIf(block -> block.getUuid().equals(removeAction.blockId()));
        } else {
            if (removeAction.blockId() == null) {
                throw new IllegalArgumentException("Block ID cannot be null");
            } else if (removeAction.blockId().isEmpty()) {
                throw new IllegalArgumentException("Block ID cannot be empty");
            }
            learningUnit.getBlocks().remove(UUID.fromString(removeAction.blockId()));
        }
    }

    private void reorderBlocks(LearningUnit learningUnit, ReorderBlockActionDTO reorderAction) {
        int newIndex = reorderAction.newIndex();
        String tempKey = reorderAction.blockId();

        UUID targetId = temporaryKeyMap.containsKey(tempKey) ? temporaryKeyMap.get(tempKey) : UUID.fromString(tempKey);

        List<Block> blocks = learningUnit.getBlocks();

        int currentIndex = -1;
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i).getUuid().equals(targetId)) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex == -1) {
            throw new IllegalArgumentException("Block not found: " + tempKey);
        }

        if (newIndex >= blocks.size()) {
            throw new IllegalArgumentException("Invalid index: " + newIndex);
        }

        Block blockToMove = blocks.remove(currentIndex);

        if (currentIndex < newIndex) {
            newIndex--;
        }

        blocks.add(newIndex, blockToMove);
    }

    private LearningUnit getLearningUnit(UUID id) {
        Optional<LearningUnit> optionalLearningUnit = findById(id);
        if (optionalLearningUnit.isEmpty()) {
            throw new IllegalArgumentException("Learning unit with id " + id + " not found");
        }
        return optionalLearningUnit.get();
    }

    private List<BlockActionDTO> filterCorrelatedActions(List<BlockActionDTO> actions) {
        Map<UUID, List<BlockActionDTO>> groupedActions = new HashMap<>();
        for (BlockActionDTO action : actions) {
            UUID key = getActionKey(action);
            if (key != null) {
                groupedActions.computeIfAbsent(key, k -> new ArrayList<>()).add(action);
            }
        }

        List<BlockActionDTO> filtered = new ArrayList<>();
        for (List<BlockActionDTO> group : groupedActions.values()) {
            boolean hasAdd = group.stream().anyMatch(a -> a instanceof AddBlockActionDTO);
            boolean hasRemove = group.stream().anyMatch(a -> a instanceof RemoveBlockActionDTO);
            if (!(hasAdd && hasRemove)) {
                filtered.addAll(group);
            }
        }
        return filtered;
    }

    private UUID getActionKey(BlockActionDTO action) {
        if (action instanceof AddBlockActionDTO addAction) {
            return UUID.fromString(addAction.blockId());
        } else if (action instanceof RemoveBlockActionDTO removeAction) {
            return UUID.fromString(removeAction.blockId());
        }
        return null;
    }
}