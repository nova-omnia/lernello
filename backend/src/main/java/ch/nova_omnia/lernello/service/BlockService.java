package ch.nova_omnia.lernello.service;

import java.util.*;

import ch.nova_omnia.lernello.dto.request.block.blockActions.*;
import ch.nova_omnia.lernello.dto.request.block.create.CreateBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.create.CreateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.create.CreateQuestionBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.create.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.update.UpdateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.update.UpdateTheoryBlockDTO;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.progress.block.BlockProgress;
import ch.nova_omnia.lernello.repository.BlockProgressRepository;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.BlockType;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.model.data.block.TranslatedBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.QuestionBlock;
import ch.nova_omnia.lernello.repository.BlockRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;
    private final BlockProgressRepository blockProgressRepository;
    private final LearningUnitRepository learningUnitRepository;

    private final Map<String, UUID> temporaryKeyMap = new HashMap<>();

    @Transactional
    public Map<String, UUID> applyBlockActions(UUID id, List<BlockActionDTO> actions) throws IllegalArgumentException {
        actions = filterCorrelatedActions(actions);

        LearningUnit learningUnit = learningUnitRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("LearningUnit not found with ID: " + id));
        temporaryKeyMap.clear();

        for (BlockActionDTO action : actions) {
            try {
                switch (action) {
                    case AddBlockActionDTO addAction -> addBlock(learningUnit, addAction);
                    case RemoveBlockActionDTO removeAction -> removeBlock(learningUnit, removeAction);
                    case ReorderBlockActionDTO reorderAction -> reorderBlocks(learningUnit, reorderAction);
                    case UpdateBlockActionDTO updateAction -> updateBlock(learningUnit, updateAction);
                    case UpdateBlockNameActionDTO updateNameAction -> updateBlockName(learningUnit, updateNameAction);
                    default -> throw new IllegalArgumentException("Unknown action type: " + action.getClass());
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to process action: " + e.getMessage(), e);
            }
        }

        List<Block> finalBlocks = learningUnit.getBlocks();
        for (int i = 0; i < finalBlocks.size(); i++) {
            Block block = finalBlocks.get(i);
            if (block.getPosition() != i) {
                block.setPosition(i);
                blockRepository.saveAndFlush(block);
            }
        }

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

        blockRepository.save(block);

        if (addAction.index() != null && addAction.index() >= 0 && addAction.index() <= learningUnit.getBlocks().size() + 1) {
            int insertionIndex = addAction.index();
            learningUnit.getBlocks().add(insertionIndex, block);
        } else {
            learningUnit.getBlocks().add(block);
        }

        if (addAction.blockId() != null) {
            temporaryKeyMap.put(addAction.blockId(), block.getUuid());
        } else {
            throw new RuntimeException("addAction.blockId() is null, which is unexpected for new blocks needing temp ID mapping.");
        }
    }

    private void removeBlock(LearningUnit learningUnit, RemoveBlockActionDTO removeAction) {
        if (removeAction.blockId() == null) {
            throw new IllegalArgumentException("Block ID cannot be null");
        } else if (removeAction.blockId().isEmpty()) {
            throw new IllegalArgumentException("Block ID cannot be empty");
        }

        UUID blockUuid;
        try {
            blockUuid = UUID.fromString(removeAction.blockId());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Block ID format: " + removeAction.blockId());
        }

        List<BlockProgress> associatedProgresses = blockProgressRepository.findByBlock_Uuid(blockUuid);
        blockProgressRepository.deleteAll(associatedProgresses);

        boolean removed = learningUnit.getBlocks().removeIf(b -> b.getUuid().equals(blockUuid));

        if (!removed) {
            System.err.println("Block with ID " + removeAction.blockId() + " not found in learning unit for removal, or already removed.");
        }
    }

    private void reorderBlocks(LearningUnit learningUnit, ReorderBlockActionDTO reorderAction) {
        int newIndex = reorderAction.newIndex();
        String tempKey = reorderAction.blockId();

        UUID targetId = temporaryKeyMap.containsKey(tempKey) ? temporaryKeyMap.get(tempKey) : UUID.fromString(tempKey);

        List<Block> blocks = learningUnit.getBlocks();

        Block blockToMove = blocks.stream().filter(block -> block.getUuid().equals(targetId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Block not found for reorder: " + tempKey + " (resolved to UUID: " + targetId + ")"));

        blocks.remove(blockToMove);

        if (newIndex <= learningUnit.getBlocks().size() + 1) {
            blocks.add(newIndex, blockToMove);
        } else {
            blocks.add(blockToMove);
        }
    }


    private void updateBlock(LearningUnit learningUnit, UpdateBlockActionDTO updateAction) {
        if (updateAction.blockId() == null) {
            throw new IllegalArgumentException("Block ID cannot be null");
        }

        Block block = blockRepository.findById(UUID.fromString(updateAction.blockId())).orElseThrow(() -> new IllegalArgumentException("Block not found"));

        if (updateAction.content() != null) {
            if (block instanceof TheoryBlock theoryBlock) {
                theoryBlock.setContent(updateAction.content());
            } else if (block instanceof TranslatedBlock translatedBlock && block.getType().equals(BlockType.THEORY)) {
                translatedBlock.setContent(updateAction.content());
            } else {
                throw new IllegalArgumentException("Content updates only supported for theory blocks");
            }
        }

        if (updateAction.question() != null) {
            if (block instanceof QuestionBlock || block instanceof TranslatedBlock) {
                QuestionBlock questionBlock = (QuestionBlock) block;
                questionBlock.setQuestion(updateAction.question());
                questionBlock.setExpectedAnswer(updateAction.expectedAnswer());
            } else if (block instanceof MultipleChoiceBlock || block instanceof TranslatedBlock) {
                MultipleChoiceBlock mcBlock = (MultipleChoiceBlock) block;
                mcBlock.setQuestion(updateAction.question());
                mcBlock.setPossibleAnswers(updateAction.possibleAnswers());
                mcBlock.setCorrectAnswers(updateAction.correctAnswers());
            } else if (block instanceof TranslatedBlock translated) {
                if (translated.getType() == BlockType.QUESTION) {
                    translated.setQuestion(updateAction.question());
                    translated.setExpectedAnswer(updateAction.expectedAnswer());
                } else if (translated.getType() == BlockType.MULTIPLE_CHOICE) {
                    translated.setQuestion(updateAction.question());
                    translated.setPossibleAnswers(updateAction.possibleAnswers());
                    translated.setCorrectAnswers(updateAction.correctAnswers());
                } else {
                    throw new IllegalArgumentException("TranslatedBlock has unsupported type for question update: " + translated.getType());
                }
            }
        }


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
    }

    private void updateBlockName(LearningUnit learningUnit, UpdateBlockNameActionDTO updateNameAction) {
        if (updateNameAction.blockId() == null) {
            throw new IllegalArgumentException("Block ID cannot be null");
        }

        Block block = blockRepository.findById(UUID.fromString(updateNameAction.blockId())).orElseThrow(() -> new IllegalArgumentException("Block not found"));
        block.setName(updateNameAction.newName());
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
