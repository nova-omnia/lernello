package ch.nova_omnia.lernello.service.block;

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
import ch.nova_omnia.lernello.model.data.block.BlockLanguage;
import ch.nova_omnia.lernello.model.data.block.BlockType;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.model.data.block.TranslatedBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.QuestionBlock;
import ch.nova_omnia.lernello.repository.BlockRepository;
import ch.nova_omnia.lernello.service.aiClient.AIClient;
import ch.nova_omnia.lernello.service.file.FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AIBlockService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BlockRepository blockRepository;
    private final FileService fileService;
    private final AIClient aiClient;

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public TheoryBlock generateTheoryBlockAI(UUID blockId, List<UUID> fileIds, String topic) {
        TheoryBlock block = (TheoryBlock) blockRepository.findById(blockId).orElseThrow(() -> new RuntimeException("Block not found: " + blockId));
        String context = fileService.extractTextFromFiles(fileIds);
        String prompt = AIPromptTemplate.THEORY_BLOCK.format(context, topic);
        String aiResponse = aiClient.sendPrompt(prompt);

        JsonNode jsonNode = parseJsonTree(aiResponse, "Failed to parse AI TheoryBlock");
        String content = jsonNode.get("content").asText();
        block.setContent(content);
        block.setName("Generated");
        block.setType(BlockType.THEORY);

        generateTranslationsParallel(block, content);
        return block;
    }

    public MultipleChoiceBlock generateMultipleChoiceBlockAI(UUID blockId, UUID theoryBlockId) {
        TheoryBlock theoryBlock = (TheoryBlock) blockRepository.findById(theoryBlockId).orElseThrow(() -> new RuntimeException("Block not found: " + theoryBlockId));
        MultipleChoiceBlock mcBlock = (MultipleChoiceBlock) blockRepository.findById(blockId).orElseThrow(() -> new RuntimeException("Block not found: " + blockId));

        String prompt = AIPromptTemplate.MULTIPLE_CHOICE.format(theoryBlock.getContent());
        String aiResponse = aiClient.sendPrompt(prompt);

        MultipleChoiceBlock generated = parseJson(aiResponse, MultipleChoiceBlock.class, "Failed to parse AI MultipleChoiceBlock");
        mcBlock.setQuestion(generated.getQuestion());
        mcBlock.setPossibleAnswers(generated.getPossibleAnswers());
        mcBlock.setCorrectAnswers(generated.getCorrectAnswers());
        mcBlock.setName("Generated");
        mcBlock.setType(BlockType.MULTIPLE_CHOICE);

        generateTranslationsParallel(mcBlock);
        return mcBlock;
    }

    public QuestionBlock generateQuestionBlockAI(UUID blockId, UUID theoryBlockId) {
        TheoryBlock theoryBlock = (TheoryBlock) blockRepository.findById(theoryBlockId).orElseThrow(() -> new RuntimeException("Block not found: " + theoryBlockId));
        QuestionBlock questionBlock = (QuestionBlock) blockRepository.findById(blockId).orElseThrow(() -> new RuntimeException("Block not found: " + blockId));

        String prompt = AIPromptTemplate.QUESTION_BLOCK.format(theoryBlock.getContent());
        String aiResponse = aiClient.sendPrompt(prompt);

        QuestionBlock generated = parseJson(aiResponse, QuestionBlock.class, "Failed to parse AI QuestionBlock");
        questionBlock.setQuestion(generated.getQuestion());
        questionBlock.setExpectedAnswer(generated.getExpectedAnswer());
        questionBlock.setName("Generated");
        questionBlock.setType(BlockType.QUESTION);

        generateTranslationsParallel(questionBlock);
        return questionBlock;
    }

    private void generateTranslationsParallel(Block block, String content) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (BlockLanguage lang : BlockLanguage.values()) {
            futures.add(CompletableFuture.runAsync(() -> {
                String prompt = AIPromptTemplate.TRANSLATION.format(lang.name(), content);
                String translated = aiClient.sendPrompt(prompt);
                TranslatedBlock translatedBlock = new TranslatedBlock();
                translatedBlock.setLanguage(lang);
                translatedBlock.setContent(translated);
                translatedBlock.setOriginalBlock(block);
                translatedBlock.setLearningUnit(block.getLearningUnit());
                translatedBlock.setPosition(block.getPosition());
                translatedBlock.setType(block.getType());
                translatedBlock.setName(aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), block.getName())));
                blockRepository.save(translatedBlock);
                blockRepository.saveAndFlush(block);
            }, executor));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void generateTranslationsParallel(QuestionBlock block) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (BlockLanguage lang : BlockLanguage.values()) {
            futures.add(CompletableFuture.runAsync(() -> {
                TranslatedBlock translatedBlock = new TranslatedBlock();
                translatedBlock.setLanguage(lang);
                translatedBlock.setQuestion(aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), block.getQuestion())));
                translatedBlock.setExpectedAnswer(aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), block.getExpectedAnswer())));
                translatedBlock.setOriginalBlock(block);
                translatedBlock.setLearningUnit(block.getLearningUnit());
                translatedBlock.setPosition(block.getPosition());
                translatedBlock.setType(block.getType());
                translatedBlock.setName(aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), block.getName())));
                blockRepository.save(translatedBlock);
                blockRepository.saveAndFlush(block);
            }, executor));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void generateTranslationsParallel(MultipleChoiceBlock block) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (BlockLanguage lang : BlockLanguage.values()) {
            futures.add(CompletableFuture.runAsync(() -> {
                TranslatedBlock translatedBlock = new TranslatedBlock();
                translatedBlock.setLanguage(lang);
                translatedBlock.setQuestion(aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), block.getQuestion())));
                translatedBlock.setPossibleAnswers(block.getPossibleAnswers().stream().map(ans -> aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), ans))).toList());
                translatedBlock.setCorrectAnswers(block.getCorrectAnswers().stream().map(ans -> aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), ans))).toList());
                translatedBlock.setOriginalBlock(block);
                translatedBlock.setLearningUnit(block.getLearningUnit());
                translatedBlock.setPosition(block.getPosition());
                translatedBlock.setType(block.getType());
                translatedBlock.setName(aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), block.getName())));
                blockRepository.save(translatedBlock);
                blockRepository.saveAndFlush(block);
            }, executor));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    @Transactional
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
                createBlocksForTopic(topicContent, topicBlocksMap.get(topicTitle), topicTitle);
            }, executor);
            futures.add(future);
        });
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void createBlocksForTopic(String content, List<Block> topicBlocks, String blockName) {
        if (content == null || content.isBlank()) return;

        try {
            List<CompletableFuture<Block>> futures = new ArrayList<>();
            futures.add(CompletableFuture.supplyAsync(() -> generateTheoryBlockFromTopic(content, blockName), executor));
            futures.add(CompletableFuture.supplyAsync(() -> {
                if (Math.random() < 0.5) {
                    return generateQuestionBlockAIFromTopic(content, blockName);
                } else {
                    return generateMultipleChoiceBlockAIFromTopic(content, blockName);
                }
            }, executor));
            List<Block> blocks = futures.stream().map(CompletableFuture::join).toList();
            topicBlocks.addAll(blocks);
        } catch (Exception e) {
            System.err.println("Failed to generate blocks for topic: " + e.getMessage());
        }
    }

    private TheoryBlock generateTheoryBlockFromTopic(String topic, String topicName) {
        TheoryBlock block = new TheoryBlock();
        String prompt = AIPromptTemplate.THEORY_BLOCK.format("", topic);
        String aiResponse = aiClient.sendPrompt(prompt);
        JsonNode jsonNode = parseJsonTree(aiResponse, "Failed to parse AI TheoryBlock for topic");
        block.setContent(jsonNode.get("content").asText());
        block.setName(topicName);
        block.setType(BlockType.THEORY);
        TheoryBlock saved = blockRepository.saveAndFlush(block);
        generateTranslationsParallel(saved, saved.getContent());
        return block;
    }

    private QuestionBlock generateQuestionBlockAIFromTopic(String topic, String topicName) {
        String prompt = AIPromptTemplate.QUESTION_BLOCK.format(topic);
        String aiResponse = aiClient.sendPrompt(prompt);
        QuestionBlock questionBlock = parseJson(aiResponse, QuestionBlock.class, "Failed to parse AI QuestionBlock from topic");
        questionBlock.setName(topicName);
        questionBlock.setType(BlockType.QUESTION);
        QuestionBlock saved = blockRepository.saveAndFlush(questionBlock);
        generateTranslationsParallel(saved);
        return questionBlock;
    }

    private MultipleChoiceBlock generateMultipleChoiceBlockAIFromTopic(String topic, String topicName) {
        String prompt = AIPromptTemplate.MULTIPLE_CHOICE.format(topic);
        String aiResponse = aiClient.sendPrompt(prompt);
        MultipleChoiceBlock mcBlock = parseJson(aiResponse, MultipleChoiceBlock.class, "Failed to parse AI MultipleChoiceBlock from topic");
        mcBlock.setName(topicName);
        mcBlock.setType(BlockType.MULTIPLE_CHOICE);
        MultipleChoiceBlock saved = blockRepository.saveAndFlush(mcBlock);
        generateTranslationsParallel(saved);
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