package ch.nova_omnia.lernello.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.block.BlockType;
import ch.nova_omnia.lernello.model.data.block.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import ch.nova_omnia.lernello.service.ai.AIClient;
import ch.nova_omnia.lernello.service.file.FileService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AIBlockService {
    private final FileService fileService;
    private final AIClient aiClient;
    private final BlockService blockService;
    private final LearningUnitRepository learningUnitRepository;

    public TheoryBlock generateTheoryBlockFromAI(List<UUID> fileIds, String topic, int position, UUID learningUnitId) {
        String context = loadContext(fileIds);
        String generatedContent = aiClient.generateTheoryBlock(context, topic);

        LearningUnit unit = learningUnitRepository.findById(learningUnitId).orElseThrow(() -> new RuntimeException());

        TheoryBlock block = new TheoryBlock(topic, position, unit, generatedContent);
        blockService.createBlock(block, learningUnitId);
        return block;
    }

    public MultipleChoiceBlock generateMultipleChoiceBlockAI(TheoryBlock theoryBlock, UUID learningUnitId) {
        String generatedContent = aiClient.generateMultipleChoiceBlock(theoryBlock.getContent());
        ObjectMapper objectMapper = new ObjectMapper();
        MultipleChoiceBlock multipleChoiceBlock;

        try {
            multipleChoiceBlock = objectMapper.readValue(generatedContent, MultipleChoiceBlock.class);
            multipleChoiceBlock.setName(theoryBlock.getName());
            multipleChoiceBlock.setPosition(theoryBlock.getPosition() + 1);
            multipleChoiceBlock.setType(BlockType.MULTIPLE_CHOICE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse AI response into MultipleChoiceBlock", e);
        }

        blockService.createBlock(multipleChoiceBlock, learningUnitId);
        return multipleChoiceBlock;
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