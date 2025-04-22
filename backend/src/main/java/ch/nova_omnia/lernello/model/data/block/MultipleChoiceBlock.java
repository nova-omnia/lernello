package ch.nova_omnia.lernello.model.data.block;

import static ch.nova_omnia.lernello.model.data.block.BlockType.MULTIPLE_CHOICE;

import java.util.ArrayList;
import java.util.List;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class MultipleChoiceBlock extends Block {

    @NotBlank
    @Column(name = "question", nullable = true)
    private String question;

    @ElementCollection
    @CollectionTable(name = "multiple_choice_possible_answers", joinColumns = @JoinColumn(name = "block_id"))
    @Column(name = "possible_answer")
    @NotEmpty
    private List<String> possibleAnswers = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "multiple_choice_correct_answers", joinColumns = @JoinColumn(name = "block_id"))
    @Column(name = "correct_answer")
    @NotEmpty
    private List<String> correctAnswers = new ArrayList<>();

    public MultipleChoiceBlock(String name, int position, LearningUnit learningUnit, String question, List<String> possibleAnswers, List<String> correctAnswers) {
        super(MULTIPLE_CHOICE, name, position, learningUnit);
        this.question = question;
        this.possibleAnswers = possibleAnswers;
        this.correctAnswers = correctAnswers;
    }
}