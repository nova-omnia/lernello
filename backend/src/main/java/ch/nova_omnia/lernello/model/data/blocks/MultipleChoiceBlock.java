package ch.nova_omnia.lernello.model.data.blocks;

import java.util.List;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("multiple_choice_block")
@NoArgsConstructor
public class MultipleChoiceBlock extends Block {

    public MultipleChoiceBlock(String name, int position, LearningUnit learningUnit) {
        super(name, position, learningUnit);
    }
    @NotNull
    @Column(name = "question", nullable = false)
    private String question;

    @ElementCollection
    @CollectionTable(name = "multiple_choice_answers", joinColumns = @JoinColumn(name = "block_id"))
    @Column(name = "answer")
    private List<String> possibleAnswers;

    @ElementCollection
    @CollectionTable(name = "multiple_choice_correct_answers", joinColumns = @JoinColumn(name = "block_id"))
    @Column(name = "correct_answer")
    private List<String> correctAnswers;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(List<String> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}