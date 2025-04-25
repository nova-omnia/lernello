package ch.nova_omnia.lernello.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.block.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.repository.BlockRepository;
import ch.nova_omnia.lernello.service.ai.AIClient;
import ch.nova_omnia.lernello.service.file.FileService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AIBlockService {
    private final FileService fileService;
    private final AIClient aiClient;
    private final BlockService blockService;
    private final BlockRepository blockRepository;

    public TheoryBlock generateTheoryBlockAI(List<UUID> fileIds, String topic,UUID blockId) {
        TheoryBlock block = (TheoryBlock) blockRepository.findById(blockId).orElseThrow(() -> new RuntimeException("Block not found"));

        String context = loadContext(fileIds);
        String generatedContent = aiClient.generateTheoryBlock(context, topic);

        block.setContent(generatedContent);
        blockRepository.save(block);
        return block;
    }

    public MultipleChoiceBlock generateMultipleChoiceBlockAI(UUID theoryBlockId, UUID learningUnitId, UUID multipleChoicheBlockUuid) {
        TheoryBlock theoryBlock = (TheoryBlock) blockService.getBlockById(theoryBlockId);
        MultipleChoiceBlock multipleChoiceBlock = (MultipleChoiceBlock) blockService.getBlockById(multipleChoicheBlockUuid);

        String generatedContent = aiClient.generateMultipleChoiceBlock(theoryBlock.getContent());
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            MultipleChoiceBlock generatedMultipleChoiceBlock = objectMapper.readValue(generatedContent, MultipleChoiceBlock.class);
            multipleChoiceBlock.setQuestion(generatedMultipleChoiceBlock.getQuestion());
            multipleChoiceBlock.setPossibleAnswers(generatedMultipleChoiceBlock.getPossibleAnswers());
            multipleChoiceBlock.setCorrectAnswers(generatedMultipleChoiceBlock.getCorrectAnswers());
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse AI response into MultipleChoiceBlock", e);
        }

        return blockService.updateMultipleChoiceBlock(multipleChoiceBlock);
    }

    private String loadContext(List<UUID> fileIds) {
        String context = "";
        for (UUID fileId : fileIds) {
            String fileContent = fileService.getFileContent(fileId);
            context = context == null ? fileContent : context + "\n" + fileContent;
        }
        return context;
    }
}