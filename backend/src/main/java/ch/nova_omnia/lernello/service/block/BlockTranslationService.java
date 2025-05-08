package ch.nova_omnia.lernello.service.block;

import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.block.LocalizedBlock;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.QuestionBlock;
import ch.nova_omnia.lernello.service.aiClient.AIClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockTranslationService {

    private final AIClient aiClient;

    private String translateText(String text, String targetLang) {
        if (text == null || text.isBlank()) return "";
        String prompt = String.format("Translate the following text to %s:\n\n%s", targetLang, text);
        return aiClient.sendPrompt(prompt).trim();
    }

    public LocalizedBlock translateTheoryBlock(TheoryBlock block, String lang) {
        LocalizedBlock lb = new LocalizedBlock();
        lb.setBlock(block);
        lb.setLanguageCode(lang);
        lb.setContent(translateText(block.getContent(), lang));
        return lb;
    }

    public LocalizedBlock translateQuestionBlock(QuestionBlock block, String lang) {
        LocalizedBlock lb = new LocalizedBlock();
        lb.setBlock(block);
        lb.setLanguageCode(lang);
        lb.setQuestion(translateText(block.getQuestion(), lang));
        lb.setExpectedAnswer(translateText(block.getExpectedAnswer(), lang));
        return lb;
    }

    public LocalizedBlock translateMultipleChoiceBlock(MultipleChoiceBlock block, String lang) {
        LocalizedBlock lb = new LocalizedBlock();
        lb.setBlock(block);
        lb.setLanguageCode(lang);
        lb.setQuestion(translateText(block.getQuestion(), lang));
        lb.setPossibleAnswers(block.getPossibleAnswers().stream().map(a -> translateText(a, lang)).toList());
        lb.setCorrectAnswers(block.getCorrectAnswers().stream().map(a -> translateText(a, lang)).toList());
        return lb;
    }
}
