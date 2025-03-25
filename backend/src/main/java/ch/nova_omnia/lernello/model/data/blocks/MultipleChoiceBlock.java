package ch.nova_omnia.lernello.model.data.blocks;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("multiple_choice_block")
public class MultipleChoiceBlock extends Block {

    public MultipleChoiceBlock(String name, int position, LearningUnit learningUnit) {
        super(name, position, learningUnit);
    }
}