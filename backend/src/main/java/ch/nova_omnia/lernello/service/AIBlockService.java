package ch.nova_omnia.lernello.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.BlockType;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.QuestionBlock;
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

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public TheoryBlock generateTheoryBlockAI(List<UUID> fileIds, String topic) {
        TheoryBlock block = new TheoryBlock();
        String context = fileService.extractTextFromFiles(fileIds);
        String prompt = AIPromptTemplate.THEORY_BLOCK.format(context, topic);
        String aiResponse = aiClient.sendPrompt(prompt);

        JsonNode jsonNode = parseJsonTree(aiResponse, "Failed to parse AI TheoryBlock");
        block.setContent(jsonNode.get("content").asText());
        return block;
    }

    public MultipleChoiceBlock generateMultipleChoiceBlockAI(UUID theoryBlockId) {
        TheoryBlock theoryBlock = (TheoryBlock) blockRepository.findById(theoryBlockId).orElseThrow(() -> new RuntimeException("Block not found: " + theoryBlockId));
        MultipleChoiceBlock mcBlock = new MultipleChoiceBlock();

        String prompt = AIPromptTemplate.MULTIPLE_CHOICE.format(theoryBlock.getContent());
        String aiResponse = aiClient.sendPrompt(prompt);

        MultipleChoiceBlock generated = parseJson(aiResponse, MultipleChoiceBlock.class, "Failed to parse AI MultipleChoiceBlock");
        mcBlock.setQuestion(generated.getQuestion());
        mcBlock.setPossibleAnswers(generated.getPossibleAnswers());
        mcBlock.setCorrectAnswers(generated.getCorrectAnswers());
        return mcBlock;
    }

    public QuestionBlock generateQuestionBlockAI(UUID theoryBlockId) {
        TheoryBlock theoryBlock = (TheoryBlock) blockRepository.findById(theoryBlockId).orElseThrow(() -> new RuntimeException("Block not found: " + theoryBlockId));
        QuestionBlock questionBlock = new QuestionBlock();

        String prompt = AIPromptTemplate.QUESTION_BLOCK.format(theoryBlock.getContent());
        String aiResponse = aiClient.sendPrompt(prompt);

        QuestionBlock generated = parseJson(aiResponse, QuestionBlock.class, "Failed to parse AI QuestionBlock");
        questionBlock.setQuestion(generated.getQuestion());
        questionBlock.setExpectedAnswer(generated.getExpectedAnswer());
        return questionBlock;
    }

    public List<Block> generateBlocksAI(List<UUID> fileIds) {
        List<Block> blocks = new ArrayList<>();
        ConcurrentHashMap<String, List<Block>> topicBlocksMap = new ConcurrentHashMap<>();

        extractTopicsFromFiles(fileIds, topicBlocksMap);

        List<Block> orderedBlocks = new ArrayList<>();
        topicBlocksMap.forEach((_, topicBlocks) -> orderedBlocks.addAll(topicBlocks));

        for (int i = 0; i < orderedBlocks.size(); i++) {
            orderedBlocks.get(i).setPosition(i);
        }

        blocks.addAll(orderedBlocks);
        return blocks;
    }

    private void extractTopicsFromFiles(List<UUID> fileIds, ConcurrentHashMap<String, List<Block>> topicBlocksMap) {
        fileIds.parallelStream().forEach(fileId -> {
            String context = fileService.extractTextFromFile(fileId);
            String prompt = AIPromptTemplate.TOPIC_EXTRACTION.format(context);

            String aiResponse = aiClient.sendPrompt(prompt);

            JsonNode jsonNode = parseJsonTree(aiResponse, "Failed to parse AI topics");
            generateBlocksFromTopics(jsonNode, topicBlocksMap);
        });
    }

    private void generateBlocksFromTopics(JsonNode jsonNode, ConcurrentHashMap<String, List<Block>> topicBlocksMap) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        jsonNode.fields().forEachRemaining(entry -> {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                String topicTitle = entry.getKey();
                String topicContent = entry.getValue().asText();

                topicBlocksMap.putIfAbsent(topicTitle, new ArrayList<>());
                createBlocksForTopic(topicContent, topicBlocksMap.get(topicTitle));
            }, executor);

            futures.add(future);
        });
        
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void createBlocksForTopic(String content, List<Block> topicBlocks) {
        if (content == null || content.isBlank()) {
            System.err.println("Content is null or empty. Cannot generate blocks.");
            return;
        }

        try {
            List<CompletableFuture<Block>> futures = new ArrayList<>();

            futures.add(CompletableFuture.supplyAsync(() -> createAndSaveBlock(generateTheoryBlockFromTopic(content), "Theory Block", BlockType.THEORY), executor));
            futures.add(CompletableFuture.supplyAsync(() -> {
                if (Math.random() < 0.5) {
                    return createAndSaveBlock(generateQuestionBlockAIFromTopic(content), "Question Block", BlockType.QUESTION);
                } else {
                    return createAndSaveBlock(generateMultipleChoiceBlockAIFromTopic(content), "Multiple Choice Block", BlockType.MULTIPLE_CHOICE);
                }
            }, executor));

            List<Block> blocks = futures.stream().map(CompletableFuture::join).toList();

            topicBlocks.addAll(blocks);

        } catch (Exception e) {
            System.err.println("Failed to generate blocks for topic: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Block createAndSaveBlock(Block block, String name, BlockType type) {
        block.setName(name);
        block.setType(type);
        return blockRepository.save(block);
    }

    private TheoryBlock generateTheoryBlockFromTopic(String topic) {
        TheoryBlock block = new TheoryBlock();
        String prompt = AIPromptTemplate.THEORY_BLOCK.format("", topic);
        String aiResponse = aiClient.sendPrompt(prompt);

        JsonNode jsonNode = parseJsonTree(aiResponse, "Failed to parse AI TheoryBlock for topic: ");
        block.setContent(jsonNode.get("content").asText());
        return block;
    }

    private QuestionBlock generateQuestionBlockAIFromTopic(String topic) {
        String prompt = AIPromptTemplate.QUESTION_BLOCK.format(topic);
        String aiResponse = aiClient.sendPrompt(prompt);

        QuestionBlock questionBlock = parseJson(aiResponse, QuestionBlock.class, "Failed to parse AI QuestionBlock from topic: ");
        return questionBlock;
    }

    private MultipleChoiceBlock generateMultipleChoiceBlockAIFromTopic(String topic) {
        String prompt = AIPromptTemplate.MULTIPLE_CHOICE.format(topic);
        String aiResponse = aiClient.sendPrompt(prompt);

        MultipleChoiceBlock mcBlock = parseJson(aiResponse, MultipleChoiceBlock.class, "Failed to parse AI MultipleChoiceBlock from topic: ");
        return mcBlock;
    }

    private <T> T parseJson(String json, Class<T> clazz, String errorMessage) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(errorMessage, e);
        }
    }

    private JsonNode parseJsonTree(String json, String errorMessage) {
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException(errorMessage, e);
        }
    }
}
