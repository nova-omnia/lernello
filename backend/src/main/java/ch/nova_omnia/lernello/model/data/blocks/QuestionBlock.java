package ch.nova_omnia.lernello.model.data.blocks;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("question_block")
@NoArgsConstructor
public class QuestionBlock extends Block {
    
    public QuestionBlock(String name, int position, LearningUnit learningUnit) {
        super(name, position, learningUnit);
    }
}