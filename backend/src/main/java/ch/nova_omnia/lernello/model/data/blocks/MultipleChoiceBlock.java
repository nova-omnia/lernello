package ch.nova_omnia.lernello.model.data.blocks;

import java.util.List;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class MultipleChoiceBlock extends Block {

    @NotBlank
    @Column(name = "question", nullable = false)
    private String question;

    @NotNull
    @Column(name = "possible_answer")
    private List<String> possibleAnswers;

    @NotNull
    @Column(name = "correct_answer")
    private List<String> correctAnswers;

    public MultipleChoiceBlock(String name, int position, BlockType blockType, LearningUnit learningUnit, String question,
    List<String> possibleAnswers, List<String> correctAnswers) {
        super(name, position,blockType, learningUnit);
        this.question = question;
        this.possibleAnswers = possibleAnswers;
        this.correctAnswers = correctAnswers;
    }
}