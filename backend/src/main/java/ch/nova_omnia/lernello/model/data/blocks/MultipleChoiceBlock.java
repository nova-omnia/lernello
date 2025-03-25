package ch.nova_omnia.lernello.model.data.blocks;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "multiple_choice_blocks")
public class MultipleChoiceBlock extends Block {

    public MultipleChoiceBlock(String name, int position, LearningUnit learningUnit) {
        super(name, position, learningUnit);
    }

}