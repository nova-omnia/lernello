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

    public TheoryBlock generateTheoryBlockFromAI(List<UUID> fileIds, String topic, int position, UUID learningUnitId) {
        String context = "";
        for (UUID fileId : fileIds) {
            String fileContent = fileService.getFileContent(fileId);
            context = context == null ? fileContent : context + "\n" + fileContent;
        }
        String content = aiClient.generateTheoryBlock(context, topic);

        LearningUnit unit = learningUnitRepository.findById(learningUnitId)
                .orElseThrow(() -> new RuntimeException());

        TheoryBlock block = new TheoryBlock(topic, position, unit, content);
        blockService.createBlock(block, learningUnitId);
        return block;
    }
}