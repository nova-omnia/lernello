package ch.nova_omnia.lernello.model.data.blocks;

import java.util.List;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class MultipleChoiceBlock extends Block {

    public MultipleChoiceBlock(String name, int position, String blockType, LearningUnit learningUnit) {
        super(name, position,blockType, learningUnit);
    }
    @NotNull
    @Column(name = "question", nullable = false)
    private String question;

    @ElementCollection
    @CollectionTable(name = "multiple_choice_answers", joinColumns = @JoinColumn(name = "block_id"))
    @Column(name = "possible_answer")
    private List<String> possibleAnswers;

    @ElementCollection
    @CollectionTable(name = "multiple_choice_correct_answers", joinColumns = @JoinColumn(name = "block_id"))
    @Column(name = "correct_answer")
    private List<String> correctAnswers;
}