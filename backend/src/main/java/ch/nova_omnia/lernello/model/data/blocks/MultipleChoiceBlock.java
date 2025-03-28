package ch.nova_omnia.lernello.model.data.blocks;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("multiple_choice_block")
@NoArgsConstructor
public class MultipleChoiceBlock extends Block {

    public MultipleChoiceBlock(String name, int position, String blockType, LearningUnit learningUnit) {
        super(name, position, blockType, learningUnit);
    }
}