package ch.nova_omnia.lernello.service.block;

import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.block.BlockLanguage;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.model.data.block.TranslatedBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.QuestionBlock;
import ch.nova_omnia.lernello.service.aiClient.AIClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockTranslationService {

    private final AIClient aiClient;

    private String translateText(String text, BlockLanguage targetLang) {
        if (text == null || text.isBlank()) return "";
        String prompt = String.format("Translate the following text to %s:\n\n%s", targetLang, text);
        return aiClient.sendPrompt(prompt).trim();
    }

    public TranslatedBlock translateTheoryBlock(TheoryBlock block, BlockLanguage lang) {
        TranslatedBlock lb = new TranslatedBlock();
        lb.setBlock(block);
        lb.setLanguage(lang);
        lb.setContent(translateText(block.getContent(), lang));
        return lb;
    }

    public TranslatedBlock translateQuestionBlock(QuestionBlock block, BlockLanguage lang) {
        TranslatedBlock lb = new TranslatedBlock();
        lb.setBlock(block);
        lb.setLanguage(lang);
        lb.setQuestion(translateText(block.getQuestion(), lang));
        lb.setExpectedAnswer(translateText(block.getExpectedAnswer(), lang));
        return lb;
    }

    public TranslatedBlock translateMultipleChoiceBlock(MultipleChoiceBlock block, BlockLanguage lang) {
        TranslatedBlock lb = new TranslatedBlock();
        lb.setBlock(block);
        lb.setLanguage(lang);
        lb.setQuestion(translateText(block.getQuestion(), lang));
        lb.setPossibleAnswers(block.getPossibleAnswers().stream().map(a -> translateText(a, lang)).toList());
        lb.setCorrectAnswers(block.getCorrectAnswers().stream().map(a -> translateText(a, lang)).toList());
        return lb;
    }
}
