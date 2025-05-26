package ch.nova_omnia.lernello.service;

import java.util.*;

import ch.nova_omnia.lernello.dto.request.block.blockActions.*;
import ch.nova_omnia.lernello.dto.request.block.create.CreateBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.create.CreateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.create.CreateQuestionBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.create.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.update.UpdateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.update.UpdateQuestionBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.update.UpdateTheoryBlockDTO;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.progress.block.BlockProgress;
import ch.nova_omnia.lernello.repository.BlockProgressRepository;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import ch.nova_omnia.lernello.repository.TranslatedBlockRepository;
import ch.nova_omnia.lernello.service.block.AIBlockService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.BlockLanguage;
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
    private final TranslatedBlockRepository translatedBlockRepository;
    private final AIBlockService aiBlockService;

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

        switch (createBlockDTO) {
            case CreateTheoryBlockDTO theoryBlockDTO -> {
                block = new TheoryBlock(theoryBlockDTO.name(), theoryBlockDTO.position(), learningUnit, theoryBlockDTO.content());
                blockRepository.save(block);
                addTranslatedBlocks(theoryBlockDTO.content(), "", "", List.of(), List.of(), block, learningUnit, theoryBlockDTO.position(), BlockType.THEORY, theoryBlockDTO.name());
            }
            case CreateMultipleChoiceBlockDTO multipleChoiceBlockDTO -> {
                block = new MultipleChoiceBlock(multipleChoiceBlockDTO.name(), multipleChoiceBlockDTO.position(), learningUnit, multipleChoiceBlockDTO.question(), multipleChoiceBlockDTO.possibleAnswers(), multipleChoiceBlockDTO.correctAnswers());
                blockRepository.save(block);
                addTranslatedBlocks("", multipleChoiceBlockDTO.question(), "", multipleChoiceBlockDTO.possibleAnswers(), multipleChoiceBlockDTO.correctAnswers(), block, learningUnit, multipleChoiceBlockDTO.position(), BlockType.MULTIPLE_CHOICE, multipleChoiceBlockDTO.name());
            }
            case CreateQuestionBlockDTO questionBlockDTO -> {
                block = new QuestionBlock(questionBlockDTO.name(), questionBlockDTO.position(), learningUnit, questionBlockDTO.question(), questionBlockDTO.expectedAnswer());
                blockRepository.save(block);
                addTranslatedBlocks("", questionBlockDTO.question(), questionBlockDTO.expectedAnswer(), List.of(), List.of(), block, learningUnit, questionBlockDTO.position(), BlockType.QUESTION, questionBlockDTO.name());
            }
            case null, default -> throw new IllegalArgumentException("Unknown block type: " + addAction.type());
        }

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

    private void addTranslatedBlocks(String content, String question, String expectedAnswer, List<String> possibleAnswers, List<String> correctAnswers, Block originalBlock, LearningUnit learningUnit, int position, BlockType type, String name) {
        List<TranslatedBlock> translatedBlocks = new ArrayList<TranslatedBlock>();

        for (BlockLanguage language : BlockLanguage.values()) {
            TranslatedBlock translatedBlock = new TranslatedBlock();
            translatedBlock.setContent(content);
            translatedBlock.setCorrectAnswers(correctAnswers);
            translatedBlock.setExpectedAnswer(expectedAnswer);
            translatedBlock.setLanguage(language);
            translatedBlock.setLearningUnit(learningUnit);
            translatedBlock.setName(name);
            translatedBlock.setOriginalBlock(originalBlock);
            translatedBlock.setPosition(position);
            translatedBlock.setPossibleAnswers(possibleAnswers);
            translatedBlock.setQuestion(question);
            translatedBlock.setType(type);

            translatedBlocks.add(translatedBlock);
        }
        blockRepository.saveAll(translatedBlocks);
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
            if (block instanceof TheoryBlock || (block instanceof TranslatedBlock && block.getType().equals(BlockType.THEORY))) {
                TheoryBlock original = (block instanceof TheoryBlock) ? (TheoryBlock) block : (TheoryBlock) ((TranslatedBlock) block).getOriginalBlock();
                String newContent = updateAction.content();

                original.setContent(aiBlockService.translateContentWithAI("ENGLISH", newContent));
                if (updateAction.data() != null) {
                    original.setName(aiBlockService.translateContentWithAI("ENGLISH", ((UpdateTheoryBlockDTO) updateAction.data()).name()));
                }

                List<TranslatedBlock> translations = translatedBlockRepository.findByOriginalBlock(original);
                for (TranslatedBlock translatedBlock : translations) {
                    String translated = aiBlockService.translateContentWithAI(translatedBlock.getLanguage().name(), newContent);
                    translatedBlock.setContent(translated);

                    if (updateAction.data() != null) {
                        translatedBlock.setName(aiBlockService.translateContentWithAI(translatedBlock.getLanguage().name(), ((UpdateTheoryBlockDTO) updateAction.data()).name()));
                    }
                }

                blockRepository.save(original);
                translatedBlockRepository.saveAll(translations);
            } else {
                throw new IllegalArgumentException("Content updates only supported for theory blocks");
            }
        }

        if (updateAction.question() != null) {
            if (block instanceof QuestionBlock || (block instanceof TranslatedBlock && block.getType().equals(BlockType.QUESTION))) {
                QuestionBlock original = (block instanceof QuestionBlock) ? (QuestionBlock) block : (QuestionBlock) ((TranslatedBlock) block).getOriginalBlock();

                original.setQuestion(aiBlockService.translateContentWithAI("ENGLISH", updateAction.question()));
                original.setExpectedAnswer(aiBlockService.translateContentWithAI("ENGLISH", updateAction.expectedAnswer()));

                if (updateAction.data() != null) {
                    original.setName(aiBlockService.translateContentWithAI("ENGLISH", ((UpdateQuestionBlockDTO) updateAction.data()).name()));
                }

                List<TranslatedBlock> translations = translatedBlockRepository.findByOriginalBlock(original);
                for (TranslatedBlock translatedBlock : translations) {
                    String translatedQuestion = aiBlockService.translateContentWithAI(translatedBlock.getLanguage().name(), updateAction.question());
                    String translatedExpectedAnswer = aiBlockService.translateContentWithAI(translatedBlock.getLanguage().name(), updateAction.expectedAnswer());
                    translatedBlock.setQuestion(translatedQuestion);
                    translatedBlock.setExpectedAnswer(translatedExpectedAnswer);

                    if (updateAction.data() != null) {
                        translatedBlock.setName(aiBlockService.translateContentWithAI(translatedBlock.getLanguage().name(), ((UpdateQuestionBlockDTO) updateAction.data()).name()));
                    }
                }

                blockRepository.save(original);
                translatedBlockRepository.saveAll(translations);
            } else
                if (block instanceof MultipleChoiceBlock || (block instanceof TranslatedBlock && block.getType().equals(BlockType.MULTIPLE_CHOICE))) {
                    MultipleChoiceBlock original = (block instanceof MultipleChoiceBlock) ? (MultipleChoiceBlock) block : (MultipleChoiceBlock) ((TranslatedBlock) block).getOriginalBlock();

                    original.setQuestion(aiBlockService.translateContentWithAI("ENGLISH", updateAction.question()));
                    original.setPossibleAnswers(aiBlockService.translateListWithAI("ENGLISH", updateAction.possibleAnswers()));
                    original.setCorrectAnswers(aiBlockService.translateListWithAI("ENGLISH", updateAction.correctAnswers()));

                    if (updateAction.data() != null) {
                        original.setName(aiBlockService.translateContentWithAI("ENGLISH", ((UpdateMultipleChoiceBlockDTO) updateAction.data()).name()));
                    }

                    List<TranslatedBlock> translations = translatedBlockRepository.findByOriginalBlock(original);
                    for (TranslatedBlock translatedBlock : translations) {
                        String translatedQuestion = aiBlockService.translateContentWithAI(translatedBlock.getLanguage().name(), updateAction.question());
                        List<String> translatedPossibleAnswers = aiBlockService.translateListWithAI(translatedBlock.getLanguage().name(), updateAction.possibleAnswers());
                        List<String> translatedCorrectAnswers = aiBlockService.translateListWithAI(translatedBlock.getLanguage().name(), updateAction.correctAnswers());
                        translatedBlock.setQuestion(translatedQuestion);
                        translatedBlock.setPossibleAnswers(translatedPossibleAnswers);
                        translatedBlock.setCorrectAnswers(translatedCorrectAnswers);

                        if (updateAction.data() != null) {
                            translatedBlock.setName(aiBlockService.translateContentWithAI(translatedBlock.getLanguage().name(), ((UpdateMultipleChoiceBlockDTO) updateAction.data()).name()));
                        }
                    }


                    blockRepository.save(original);
                    translatedBlockRepository.saveAll(translations);
                } else {
                    throw new IllegalArgumentException("Unsupported block type for question update");
                }
        }
    }

    private void updateBlockName(LearningUnit learningUnit, UpdateBlockNameActionDTO updateNameAction) {
        if (updateNameAction.blockId() == null) {
            throw new IllegalArgumentException("Block ID cannot be null");
        }

        Block block = blockRepository.findById(UUID.fromString(updateNameAction.blockId())).orElseThrow(() -> new IllegalArgumentException("Block not found"));

        Block original = block instanceof TranslatedBlock ? ((TranslatedBlock) block).getOriginalBlock() : block;
        original.setName(aiBlockService.translateContentWithAI("ENGLISH", updateNameAction.newName()));

        List<TranslatedBlock> translations = translatedBlockRepository.findByOriginalBlock(original);
        for (TranslatedBlock internalTranslatedBlock : translations) {
            internalTranslatedBlock.setName(aiBlockService.translateContentWithAI(internalTranslatedBlock.getLanguage().name(), updateNameAction.newName()));
        }

        blockRepository.save(original);
        translatedBlockRepository.saveAll(translations);
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
