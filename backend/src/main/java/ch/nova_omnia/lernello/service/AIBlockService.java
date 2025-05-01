package ch.nova_omnia.lernello.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public TheoryBlock generateTheoryBlockAI(List<UUID> fileIds, String topic) {
        TheoryBlock block = new TheoryBlock();

        String context = fileService.extractTextFromFiles(fileIds);
        String prompt = buildTheoryBlockPrompt(context, topic, block.getPosition());
        String aiResponse = aiClient.sendPrompt(prompt);

        try {
            JsonNode jsonNode = objectMapper.readTree(aiResponse);
            String content = jsonNode.get("content").asText();
            block.setContent(content);
            return block;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI TheoryBlock", e);
        }
    }

    public MultipleChoiceBlock generateMultipleChoiceBlockAI(UUID theoryBlockId) {
        TheoryBlock theoryBlock = (TheoryBlock) getBlockById(theoryBlockId);
        MultipleChoiceBlock mcBlock = new MultipleChoiceBlock();

        String prompt = buildMultipleChoicePrompt(theoryBlock.getContent());
        String aiResponse = aiClient.sendPrompt(prompt);

        try {
            MultipleChoiceBlock generated = objectMapper.readValue(aiResponse, MultipleChoiceBlock.class);
            mcBlock.setQuestion(generated.getQuestion());
            mcBlock.setPossibleAnswers(generated.getPossibleAnswers());
            mcBlock.setCorrectAnswers(generated.getCorrectAnswers());
            return mcBlock;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI MultipleChoiceBlock", e);
        }
    }

    public QuestionBlock generateQuestionBlockAI(UUID theoryBlockUuid) {
        TheoryBlock theoryBlock = (TheoryBlock) getBlockById(theoryBlockUuid);
        QuestionBlock questionBlock = new QuestionBlock();

        String prompt = buildQuestionBlockPrompt(theoryBlock.getContent());
        String aiResponse = aiClient.sendPrompt(prompt);

        try {
            QuestionBlock generated = objectMapper.readValue(aiResponse, QuestionBlock.class);
            questionBlock.setQuestion(generated.getQuestion());
            questionBlock.setExpectedAnswer(generated.getExpectedAnswer());
            return questionBlock;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI QuestionBlock", e);
        }
    }

    @Transactional
    private Block getBlockById(UUID blockId) {
        return blockRepository.findById(blockId).orElseThrow(() -> new RuntimeException("Block not found" + blockId));
    }

    private String buildTheoryBlockPrompt(String context, String topic, int position) {
        return """
                You are an AI tutor. Create a theory block on the topic '%s' strictly based on the provided content.
                The Theory Block should have a maximum of 100 Words.
                Content:
                %s
                Respond with pure JSON:
                { "content": "..." }

                The theory must be adapted to fit the given position in a sequence of theory blocks.
                Take into account that there may be previous or subsequent theory blocks, so your text should be naturally integrated into a larger flow.
                You must not simply repeat the same summary for different positions. Adjust the phrasing, transitions, and focus accordingly, but always stay based strictly on the provided content.

                Position of this Theory Block: %s
                """.formatted(topic, context, position);
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
