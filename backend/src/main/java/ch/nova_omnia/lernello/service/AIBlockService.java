package ch.nova_omnia.lernello.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.BlockType;
import ch.nova_omnia.lernello.model.data.block.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.QuestionBlock;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.repository.BlockRepository;
import ch.nova_omnia.lernello.service.aiClient.AIClient;
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
            block.setContent(jsonNode.get("content").asText());
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

    public List<Block> generateBlocksAI(List<UUID> fileIds) {
        String context = fileService.extractTextFromFiles(fileIds);
        String prompt = buildSmartUnitPrompt(context);
        String aiResponse = aiClient.sendPrompt(prompt);
        System.out.println("AI Response: " + aiResponse);

        try {
            JsonNode json = parseToJsonArray(aiResponse);
            if (!json.isArray()) {
                throw new RuntimeException("AI response is not a valid JSON array");
            }

            List<Block> blocks = new ArrayList<>();
            int position = 0;

            for (JsonNode node : json) {
                Block block = BlockFactory.create(node, position);
                if (block != null) {
                    blocks.add(block);
                    position++;
                }
            }

            return blocks;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process AI response JSON", e);
        }
    }

    @Transactional
    private Block getBlockById(UUID blockId) {
        return blockRepository.findById(blockId).orElseThrow(() -> new RuntimeException("Block not found: " + blockId));
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

    private String buildSmartUnitPrompt(String context) {
        return """
                You are an AI tutor.

                Create a structured learning unit based strictly on the provided content.
                The unit must consist the provided blocks. Each block must be one of the following types:

                - "Theory": Explain concepts concisely.
                - "MultipleChoice": Provide a question with multiple answers and mark the correct ones.
                - "Question": Ask an open-ended question expecting a text-based answer.

                Important:
                - Every block **must** include a "type" key with one of the values: "Theory", "MultipleChoice", or "Question".
                - Respond with pure JSON only — a flat list ([]) of block objects.
                - Do not include any explanations, markdown, or text outside the JSON.

                Respond with pure JSON:

                [
                    { "type": "Theory", "content": "..." },
                    { "type": "MultipleChoice", "question": "...", "possibleAnswers": ["..."], "correctAnswers": ["..."] },
                    { "type": "Question", "question": "...", "expectedAnswer": "..." }
                ]

                Content:
                %s
                """.formatted(context);
    }

    private JsonNode parseToJsonArray(String rawJson) throws JsonProcessingException {
        String trimmed = rawJson.trim();

        if (!trimmed.startsWith("[")) {
            String fixed = "[" + trimmed;
            if (trimmed.endsWith(",")) {
                fixed = fixed.substring(0, fixed.length() - 1);
            }
            if (!trimmed.endsWith("}")) {
                fixed = fixed + "}";
            }
            fixed = fixed + "]";
            return objectMapper.readTree(fixed);
        }

        return objectMapper.readTree(trimmed);
    }

    private static class BlockFactory {
        static Block create(JsonNode node, int position) {
            try {
                String type = getTextSafe(node, "type");
                if (type == null) return null;

                return switch (type) {
                    case "Theory" -> {
                        TheoryBlock block = new TheoryBlock();
                        block.setName("Theory");
                        block.setType(BlockType.THEORY);
                        block.setContent(getTextSafe(node, "content", "..."));
                        block.setPosition(position);
                        yield block;
                    }
                    case "MultipleChoice" -> {
                        MultipleChoiceBlock block = new MultipleChoiceBlock();
                        block.setName("Multiple Choice");
                        block.setType(BlockType.MULTIPLE_CHOICE);
                        block.setQuestion(getTextSafe(node, "question", "No question provided"));
                        block.setPossibleAnswers(toListSafe(node.get("possibleAnswers")));
                        block.setCorrectAnswers(toListSafe(node.get("correctAnswers")));
                        block.setPosition(position);
                        yield block;
                    }
                    case "Question" -> {
                        QuestionBlock block = new QuestionBlock();
                        block.setName("Question");
                        block.setType(BlockType.QUESTION);
                        block.setQuestion(getTextSafe(node, "question", "No question provided"));
                        block.setExpectedAnswer(getTextSafe(node, "expectedAnswer", "No answer provided"));
                        block.setPosition(position);
                        yield block;
                    }
                    default -> {
                        System.out.println("Unknown block type: " + type);
                        yield null;
                    }
                };
            } catch (Exception e) {
                System.out.println("Skipping invalid block: " + node.toPrettyString() + " – Reason: " + e.getMessage());
                return null;
            }
        }

        private static String getTextSafe(JsonNode node, String field) {
            return getTextSafe(node, field, null);
        }

        private static String getTextSafe(JsonNode node, String field, String fallback) {
            JsonNode value = node.get(field);
            if (value != null && !value.isNull() && value.isTextual()) {
                return value.asText();
            }
            return fallback;
        }

        private static List<String> toListSafe(JsonNode arrayNode) {
            List<String> list = new ArrayList<>();
            if (arrayNode != null && arrayNode.isArray()) {
                for (JsonNode item : arrayNode) {
                    if (item.isTextual()) {
                        list.add(item.asText());
                    }
                }
            }
            return list;
        }
    }
}
