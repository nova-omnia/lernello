package ch.nova_omnia.lernello.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

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
        String prompt = buildTheoryBlockPrompt(context, topic);
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
        TheoryBlock theoryBlock = (TheoryBlock) blockRepository.findById(theoryBlockId).orElseThrow(() -> new RuntimeException("Block not found: " + theoryBlockId));
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

    public QuestionBlock generateQuestionBlockAI(UUID theoryBlockId) {
        TheoryBlock theoryBlock = (TheoryBlock) blockRepository.findById(theoryBlockId).orElseThrow(() -> new RuntimeException("Block not found: " + theoryBlockId));
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
        List<Block> blocks = new ArrayList<>();
        extractTopicsFromFile(fileIds, blocks);
        return blocks;
    }

    private TheoryBlock generateTheoryBlockFromTopic(String topic) {
        TheoryBlock block = new TheoryBlock();
        String prompt = buildTheoryBlockPrompt("", topic);
        String aiResponse = aiClient.sendPrompt(prompt);

        try {
            JsonNode jsonNode = objectMapper.readTree(aiResponse);
            block.setContent(jsonNode.get("content").asText());
            return block;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI TheoryBlock for topic: " + topic, e);
        }
    }

    private void extractTopicsFromFile(List<UUID> fileIds, List<Block> blocks) {
        // Use a thread-safe map to store blocks grouped by topics
        ConcurrentHashMap<String, List<Block>> topicBlocksMap = new ConcurrentHashMap<>();

        fileIds.parallelStream().forEach(fileId -> {
            String context = fileService.extractTextFromFile(fileId);
            String prompt = """
                You are an AI assistant. Analyze the following content and divide it into meaningful and complete topics or sections.
                Each topic should have a title and the corresponding text. Do not omit or truncate any part of the content.
                Ensure that the topics are comprehensive and cover the entire content.

                Content:
                %s

                Respond with pure JSON:
                {
                    "topic1_title": "topic1_text",
                    "topic2_title": "topic2_text",
                    ...
                }
                """.formatted(context);

            String aiResponse = aiClient.sendPrompt(prompt);
            try {
                JsonNode jsonNode = objectMapper.readTree(aiResponse);
                jsonNode.fieldNames().forEachRemaining(topic -> {
                    topicBlocksMap.putIfAbsent(topic, new ArrayList<>());
                    createBlocksForTopic(topic, topicBlocksMap.get(topic));
                });
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse AI topics", e);
            }
        });

        // Flatten the map into a single list and assign positions
        List<Block> orderedBlocks = new ArrayList<>();
        topicBlocksMap.forEach((topic, topicBlocks) -> orderedBlocks.addAll(topicBlocks));

        for (int i = 0; i < orderedBlocks.size(); i++) {
            orderedBlocks.get(i).setPosition(i);
        }

        blocks.addAll(orderedBlocks);
    }

    private void createBlocksForTopic(String topic, List<Block> topicBlocks) {
        try {
            TheoryBlock theoryBlock = generateTheoryBlockFromTopic(topic);
            theoryBlock.setName("Theory Block");
            theoryBlock.setType(BlockType.THEORY);
            blockRepository.save(theoryBlock);
            topicBlocks.add(theoryBlock);

            if (Math.random() < 0.5) {
                QuestionBlock quizBlock = generateQuestionBlockAIFromTopic(topic);
                quizBlock.setName("Question Block");
                quizBlock.setType(BlockType.QUESTION);
                blockRepository.save(quizBlock);
                topicBlocks.add(quizBlock);
            } else {
                MultipleChoiceBlock quizBlock = generateMultipleChoiceBlockAIFromTopic(topic);
                quizBlock.setName("Multiple Choice Block");
                quizBlock.setType(BlockType.MULTIPLE_CHOICE);
                blockRepository.save(quizBlock);
                topicBlocks.add(quizBlock);
            }
        } catch (Exception e) {
            System.err.println("Failed to generate blocks for topic: " + topic + " - " + e.getMessage());
        }
    }

    private QuestionBlock generateQuestionBlockAIFromTopic(String topic) {
        String prompt = buildQuestionBlockPrompt(topic);
        String aiResponse = aiClient.sendPrompt(prompt);

        try {
            QuestionBlock questionBlock = objectMapper.readValue(aiResponse, QuestionBlock.class);
            return questionBlock;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI QuestionBlock from topic: " + topic, e);
        }
    }

    private MultipleChoiceBlock generateMultipleChoiceBlockAIFromTopic(String topic) {
        String prompt = buildMultipleChoicePrompt(topic);
        String aiResponse = aiClient.sendPrompt(prompt);

        try {
            MultipleChoiceBlock mcBlock = objectMapper.readValue(aiResponse, MultipleChoiceBlock.class);
            return mcBlock;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI MultipleChoiceBlock from topic: " + topic, e);
        }
    }

    private String buildTheoryBlockPrompt(String context, String topic) {
        return """
                You are an AI tutor. Create a theory block on the topic '%s' strictly based on the provided content.
                The Theory Block should have a maximum of 100 Words.
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

                Ensure that the question is not longer than 200 characters and the possibleAnswers and correctAnswers are not longer than 100 characters each.
                """.formatted(content);
    }

    private String buildQuestionBlockPrompt(String content) {
        return """
                Based on the following content, create a question.
                Content:
                %s
                Respond with pure JSON:
                { "question": "...", "expectedAnswer": "..." }

                Ensure that both the question and the expectedAnswer are not longer than 200 characters each.
                """.formatted(content);
    }
}
