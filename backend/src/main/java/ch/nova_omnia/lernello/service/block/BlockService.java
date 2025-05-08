// src/main/java/ch/nova_omnia/lernello/service/BlockService.java
package ch.nova_omnia.lernello.service.block;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.dto.request.block.update.UpdateBlockOrderDTO;
import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.QuestionBlock;
import ch.nova_omnia.lernello.repository.BlockRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;

    @Transactional
    public <T extends Block> T createBlock(T block, UUID learningUnitId) {
        rearrangeBlockPosition(block.getPosition(), learningUnitId);
        return blockRepository.save(block);
    }

    public List<Block> getBlocksByLearningUnit(UUID learningUnitId) {
        return blockRepository.findBlocksAsc(learningUnitId);
    }

    @Transactional
    public void updateBlockOrder(UpdateBlockOrderDTO updateBlockOrderDTO) {
        List<UUID> blockIds = updateBlockOrderDTO.blockUuidsInOrder();
        for (int i = 0; i < blockIds.size(); i++) {
            Block block = blockRepository.findById(blockIds.get(i)).orElseThrow(() -> new RuntimeException("Block not found"));
            blockRepository.updatePosition(i, block.getUuid());
        }
    }

    @Transactional
    public Block getBlockById(UUID blockId) {
        return blockRepository.findById(blockId).orElseThrow(() -> new RuntimeException("Block not found"));
    }

    private void rearrangeBlockPosition(int position, UUID learningUnitUuid) {
        List<Block> blocks = blockRepository.findBlocksAsc(learningUnitUuid);
        for (Block block : blocks) {
            if (block.getPosition() >= position) {
                blockRepository.updatePosition(position, block.getUuid());
            }
        }
    }

    public void deleteBlockById(UUID blockId) {
        blockRepository.deleteById(blockId);
    }

    @Transactional
    public TheoryBlock updateTheoryBlock(TheoryBlock theoryBlock) {
        TheoryBlock existingBlock = (TheoryBlock) blockRepository.findById(theoryBlock.getUuid()).orElseThrow(() -> new RuntimeException("TheoryBlock not found"));
        existingBlock.setName(theoryBlock.getName());
        existingBlock.setContent(theoryBlock.getContent());
        return blockRepository.save(existingBlock);
    }

    @Transactional
    public QuestionBlock updateQuestionBlock(QuestionBlock questionBlock) {
        QuestionBlock existingBlock = (QuestionBlock) blockRepository.findById(questionBlock.getUuid()).orElseThrow(() -> new RuntimeException("QuestionBlock not found"));
        existingBlock.setName(questionBlock.getName());
        existingBlock.setQuestion(questionBlock.getQuestion());
        existingBlock.setExpectedAnswer(questionBlock.getExpectedAnswer());
        return blockRepository.save(existingBlock);
    }

    @Transactional
    public MultipleChoiceBlock updateMultipleChoiceBlock(MultipleChoiceBlock multipleChoiceBlock) {
        MultipleChoiceBlock existingBlock = (MultipleChoiceBlock) blockRepository.findById(multipleChoiceBlock.getUuid()).orElseThrow(() -> new RuntimeException("MultipleChoiceBlock not found"));
        existingBlock.setName(multipleChoiceBlock.getName());
        existingBlock.setQuestion(multipleChoiceBlock.getQuestion());
        existingBlock.setPossibleAnswers(multipleChoiceBlock.getPossibleAnswers());
        existingBlock.setCorrectAnswers(multipleChoiceBlock.getCorrectAnswers());
        return blockRepository.save(existingBlock);
    }
}
