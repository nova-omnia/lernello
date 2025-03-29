package ch.nova_omnia.lernello.model.data.blocks;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class QuestionBlock extends Block {
    
    public QuestionBlock(String name, int position, String blockType, LearningUnit learningUnit) {
        super(name, position,blockType, learningUnit);
    }
    
    @NotNull
    @Column(name = "question", nullable = false)
    private String question;

    @NotNull
    @Column(name = "expected_answer", nullable = false)
    private String expectedAnswer;

}