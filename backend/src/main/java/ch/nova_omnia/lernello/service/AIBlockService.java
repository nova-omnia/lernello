package ch.nova_omnia.lernello.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.LearningUnit;
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

    public TheoryBlock createAiTheoryBlock(TheoryBlock block, UUID learningUnitId, List<UUID> fileIds) {
        String context = loadContext(fileIds);
        String topic = block.getName();

        String generatedContent = aiClient.generateTheoryBlock(context, topic);

        LearningUnit unit = learningUnitRepository.findById(learningUnitId).orElseThrow(() -> new RuntimeException());

        TheoryBlock theoryBlock = new TheoryBlock(topic, block.getPosition(), unit, generatedContent);
        blockService.createBlock(theoryBlock, learningUnitId);
        return theoryBlock;
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