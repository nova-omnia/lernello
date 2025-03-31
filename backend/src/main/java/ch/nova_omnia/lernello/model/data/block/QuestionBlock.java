package ch.nova_omnia.lernello.model.data.block;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class QuestionBlock extends Block {

    @NotBlank
    @Column(name = "question", nullable = false)
    private String question;

    @NotBlank
    @Column(name = "expected_answer", nullable = false)
    private String expectedAnswer;

    public QuestionBlock(String name, int position, BlockType blockType, LearningUnit learningUnit, String question,
            String expectedAnswer) {
        super(name, position,blockType, learningUnit);
        this.question = question;
        this.expectedAnswer = expectedAnswer;
    }
}