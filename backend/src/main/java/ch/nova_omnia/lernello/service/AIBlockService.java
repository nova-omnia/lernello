package ch.nova_omnia.lernello.service;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.QuestionBlock;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.repository.BlockRepository;
import ch.nova_omnia.lernello.service.ai.AIClient;
import ch.nova_omnia.lernello.service.file.FileService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AIBlockService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BlockRepository blockRepository;
    private final FileService fileService;
    private final AIClient aiClient;

    public TheoryBlock generateTheoryBlockAI(List<UUID> fileIds, String topic, UUID blockId) {
        TheoryBlock block = (TheoryBlock) blockRepository.findById(blockId).orElseThrow(() -> new RuntimeException("Block not found"));

        String context = fileService.extractTextFromFiles(fileIds);
        String prompt = buildTheoryBlockPrompt(context, topic);
        String aiResponse = aiClient.sendPrompt(prompt);

        try {
            TheoryBlock generated = objectMapper.readValue(aiResponse, TheoryBlock.class);
            block.setContent(generated.getContent());
            return blockRepository.save(block);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI TheoryBlock", e);
        }
    }

    public MultipleChoiceBlock generateMultipleChoiceBlockAI(UUID theoryBlockId, UUID multipleChoiceBlockUuid) {
        TheoryBlock theoryBlock = (TheoryBlock) getBlockById(theoryBlockId);
        MultipleChoiceBlock mcBlock = (MultipleChoiceBlock) getBlockById(multipleChoiceBlockUuid);

        String prompt = buildMultipleChoicePrompt(theoryBlock.getContent());
        String aiResponse = aiClient.sendPrompt(prompt);

        try {
            MultipleChoiceBlock generated = objectMapper.readValue(aiResponse, MultipleChoiceBlock.class);
            mcBlock.setQuestion(generated.getQuestion());
            mcBlock.setPossibleAnswers(generated.getPossibleAnswers());
            mcBlock.setCorrectAnswers(generated.getCorrectAnswers());
            return blockRepository.save(mcBlock);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI MultipleChoiceBlock", e);
        }
    }

    public QuestionBlock generateQuestionBlockAI(UUID theoryBlockUuid, UUID questionBlockUuid) {
        TheoryBlock theoryBlock = (TheoryBlock) getBlockById(theoryBlockUuid);
        QuestionBlock questionBlock = (QuestionBlock) getBlockById(questionBlockUuid);

        String prompt = buildQuestionBlockPrompt(theoryBlock.getContent());
        String aiResponse = aiClient.sendPrompt(prompt);

        try {
            QuestionBlock generated = objectMapper.readValue(aiResponse, QuestionBlock.class);
            questionBlock.setQuestion(generated.getQuestion());
            questionBlock.setExpectedAnswer(generated.getExpectedAnswer());
            return blockRepository.save(questionBlock);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI QuestionBlock", e);
        }
    }

    @Transactional
    private Block getBlockById(UUID blockId) {
        return blockRepository.findById(blockId).orElseThrow(() -> new RuntimeException("Block not found" + blockId));
    }

    private String buildTheoryBlockPrompt(String context, String topic) {
        return """
                You are an AI tutor. Create a theory block on the topic '%s' strictly based on the provided content.
                Do not use any external knowledge or your own database.
                Content:
                %s
                Respond with pure JSON:
                { "content": "..." }
                """.formatted(topic, context);
    }

    private String buildMultipleChoicePrompt(String content) {
        return """
                Based on the following content, create a multiple choice question.
                Content:
                %s
                Respond with pure JSON:
                { "question": "...", "possibleAnswers": ["..."], "correctAnswers": ["..."] }
                """.formatted(content);
    }

    private String buildQuestionBlockPrompt(String content) {
        return """
                Based on the following content, create a question.
                Content:
                %s
                Respond with pure JSON:
                { "question": "...", "expectedAnswer": "..." }
                """.formatted(content);
    }
}
