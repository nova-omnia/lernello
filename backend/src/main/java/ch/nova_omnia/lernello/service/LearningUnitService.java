package ch.nova_omnia.lernello.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.dto.request.block.blockActions.AddBlockActionDTO;
import ch.nova_omnia.lernello.dto.request.block.blockActions.BlockActionDTO;
import ch.nova_omnia.lernello.dto.request.block.blockActions.RemoveBlockActionDTO;
import ch.nova_omnia.lernello.dto.request.block.blockActions.ReorderBlockActionDTO;
import ch.nova_omnia.lernello.dto.request.block.blockActions.UpdateBlockActionDTO;
import ch.nova_omnia.lernello.dto.request.block.create.CreateBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.create.CreateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.create.CreateQuestionBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.create.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.update.UpdateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.update.UpdateTheoryBlockDTO;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.QuestionBlock;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.repository.BlockRepository;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearningUnitService {
    private final LearningUnitRepository learningUnitRepository;
    private final BlockRepository blockRepository;
    private final AIBlockService aiBlockService;

    private Map<String, UUID> temporaryKeyMap = new HashMap<>();

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
    public LearningUnit generateLearningUnitWithAI(List<UUID> fileIds, UUID learningUnitId) {
        LearningUnit learningUnit = getLearningUnit(learningUnitId);
        
        // TODO: discuss, if we want to keep the blocks in the learning unit
        // or if we want to replace them with the new ones
        learningUnit.getBlocks().clear();

        List<Block> blocks = aiBlockService.generateBlocksAI(fileIds);

        for (Block block : blocks) {
            block.setLearningUnit(learningUnit);
            learningUnit.getBlocks().add(block);
        }

        return learningUnitRepository.save(learningUnit);
    }

    @Transactional
    public Map<String, UUID> applyBlockActions(UUID id, List<BlockActionDTO> actions) throws IllegalArgumentException {
        actions = filterCorrelatedActions(actions);

        LearningUnit learningUnit = getLearningUnit(id);
        temporaryKeyMap.clear();

        for (BlockActionDTO action : actions) {
            try {
                switch (action) {
                    case AddBlockActionDTO addAction -> addBlock(learningUnit, addAction);
                    case RemoveBlockActionDTO removeAction -> removeBlock(learningUnit, removeAction);
                    case ReorderBlockActionDTO reorderAction -> reorderBlocks(learningUnit, reorderAction);
                    case UpdateBlockActionDTO updateAction -> updateBlock(learningUnit, updateAction);
                    default -> throw new IllegalArgumentException("Unknown action type: " + action.getClass());
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to process action: " + e.getMessage(), e);
            }
        }

        blockRepository.flush();
        learningUnitRepository.saveAndFlush(learningUnit);
        return temporaryKeyMap;
    }

    private void addBlock(LearningUnit learningUnit, AddBlockActionDTO addAction) throws IllegalArgumentException {
        Block block = null;
        CreateBlockDTO createBlockDTO = addAction.data();

        block = switch (createBlockDTO) {
            case CreateTheoryBlockDTO theoryBlockDTO -> new TheoryBlock(
                    theoryBlockDTO.name(), theoryBlockDTO.position(), learningUnit, theoryBlockDTO.content()
            );
            case CreateMultipleChoiceBlockDTO multipleChoiceBlockDTO -> new MultipleChoiceBlock(
                    multipleChoiceBlockDTO.name(), multipleChoiceBlockDTO.position(), learningUnit, multipleChoiceBlockDTO.question(), multipleChoiceBlockDTO.possibleAnswers(), multipleChoiceBlockDTO.correctAnswers()
            );
            case CreateQuestionBlockDTO questionBlockDTO -> new QuestionBlock(
                    questionBlockDTO.name(), questionBlockDTO.position(), learningUnit, questionBlockDTO.question(), questionBlockDTO.expectedAnswer()
            );
            case null, default -> throw new IllegalArgumentException("Unknown block type: " + addAction.type());
        };

        if (addAction.index() != null) {
            learningUnit.getBlocks().add(addAction.index(), block);
        } else {
            learningUnit.getBlocks().add(block);
        }

        blockRepository.saveAndFlush(block);

        if (addAction.blockId() != null) {
            temporaryKeyMap.put(addAction.blockId(), block.getUuid());
        } else {
            throw new RuntimeException("addAction is null");
        }
    }

    private void removeBlock(LearningUnit learningUnit, RemoveBlockActionDTO removeAction) {
        if (removeAction.blockId() == null) {
            throw new IllegalArgumentException("Block ID cannot be null");
        } else if (removeAction.blockId().isEmpty()) {
            throw new IllegalArgumentException("Block ID cannot be empty");
        }

        UUID blockUuid = UUID.fromString(removeAction.blockId());
        boolean removed = learningUnit.getBlocks().removeIf(block -> block.getUuid().equals(blockUuid));

        if (!removed) {
            throw new IllegalArgumentException("Block with ID " + removeAction.blockId() + " not found");
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

        if (newIndex < 0) {
            throw new IllegalArgumentException("New index cannot be negative");
        }
        if (newIndex > blocks.size() - 1) {
            newIndex = blocks.size() - 1;
        }
        if (currentIndex < newIndex) {
            newIndex = newIndex - 1;
        }

        Block blockToMove = blocks.remove(currentIndex);
        blocks.add(newIndex, blockToMove);

        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            block.setPosition(i);
            blockRepository.save(block);
        }

        blockRepository.flush();
    }


    private void updateBlock(LearningUnit learningUnit, UpdateBlockActionDTO updateAction) {
        if (updateAction.blockId() == null) {
            throw new IllegalArgumentException("Block ID cannot be null");
        }

        Block block = blockRepository.findById(UUID.fromString(updateAction.blockId())).orElseThrow(() -> new IllegalArgumentException("Block not found"));

        // Handle direct field updates
        if (updateAction.content() != null) {
            if (block instanceof TheoryBlock theoryBlock) {
                theoryBlock.setContent(updateAction.content());
            } else {
                throw new IllegalArgumentException("Content updates only supported for theory blocks");
            }
        }

        if (updateAction.question() != null) {
            if (block instanceof QuestionBlock questionBlock) {
                questionBlock.setQuestion(updateAction.question());
                questionBlock.setExpectedAnswer(updateAction.expectedAnswer());
            } else if (block instanceof MultipleChoiceBlock mcBlock) {
                mcBlock.setQuestion(updateAction.question());
                mcBlock.setPossibleAnswers(updateAction.possibleAnswers());
                mcBlock.setCorrectAnswers(updateAction.correctAnswers());

            }
        }
        // Handle full DTO updates if present
        if (updateAction.data() != null) {
            switch (updateAction.data()) {
                case UpdateTheoryBlockDTO theoryBlockDTO -> {
                    TheoryBlock theoryBlock = (TheoryBlock) block;
                    if (theoryBlockDTO.name() != null) {
                        theoryBlock.setName(theoryBlockDTO.name());
                    }
                    if (theoryBlockDTO.content() != null) {
                        theoryBlock.setContent(theoryBlockDTO.content());
                    }
                }
                case UpdateMultipleChoiceBlockDTO mcBlockDTO -> {
                    MultipleChoiceBlock mcBlock = (MultipleChoiceBlock) block;
                    if (mcBlockDTO.name() != null) {
                        mcBlock.setName(mcBlockDTO.name());
                    }
                    if (mcBlockDTO.question() != null) {
                        mcBlock.setQuestion(mcBlockDTO.question());
                    }
                    if (mcBlockDTO.possibleAnswers() != null) {
                        mcBlock.setPossibleAnswers(mcBlockDTO.possibleAnswers());
                    }
                    if (mcBlockDTO.correctAnswers() != null) {
                        mcBlock.setCorrectAnswers(mcBlockDTO.correctAnswers());
                    }
                }
                case null, default -> throw new IllegalArgumentException("Unknown block type in update");
            }
        }

        blockRepository.saveAndFlush(block);
    }

    private LearningUnit getLearningUnit(UUID id) {
        Optional<LearningUnit> optionalLearningUnit = findById(id);
        if (optionalLearningUnit.isEmpty()) {
            throw new IllegalArgumentException("Learning unit with id " + id + " not found");
        }
        return optionalLearningUnit.get();
    }

    private List<BlockActionDTO> filterCorrelatedActions(List<BlockActionDTO> actions) {
        Map<String, List<BlockActionDTO>> groupedActions = new HashMap<>();
        List<BlockActionDTO> nonGroupedActions = new ArrayList<>();

        for (BlockActionDTO action : actions) {
            String key = getActionKey(action);
            if (key != null) {
                groupedActions.computeIfAbsent(key, _ -> new ArrayList<>()).add(action);
            } else {
                nonGroupedActions.add(action);
            }
        }

        List<BlockActionDTO> filtered = new ArrayList<>(nonGroupedActions);

        for (List<BlockActionDTO> group : groupedActions.values()) {
            boolean blockExists = false;
            for (BlockActionDTO action : group) {
                if (action instanceof AddBlockActionDTO) {
                    blockExists = true;
                } else if (action instanceof RemoveBlockActionDTO) {
                    blockExists = false;
                }
            }
            if (blockExists) {
                filtered.addAll(group);
            }
        }

        return filtered;
    }

    private String getActionKey(BlockActionDTO action) {
        if (action instanceof AddBlockActionDTO addAction) {
            if (isTemporaryId(addAction.blockId())) {
                return addAction.blockId();
            }
        } else if (action instanceof RemoveBlockActionDTO removeAction) {
            if (isTemporaryId(removeAction.blockId())) {
                return removeAction.blockId();
            }
        } else if (action instanceof ReorderBlockActionDTO reorderAction) {
            if (isTemporaryId(reorderAction.blockId())) {
                return reorderAction.blockId();
            }
        }
        return null;
    }

    private boolean isTemporaryId(String id) {
        return id != null && id.startsWith("tempid:");
    }
}
