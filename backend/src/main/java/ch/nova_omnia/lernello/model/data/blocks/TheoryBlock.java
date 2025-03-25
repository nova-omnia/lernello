package ch.nova_omnia.lernello.model.data.blocks;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("theory_block")
@NoArgsConstructor
public class TheoryBlock extends Block {
    
    public TheoryBlock(String name, int position, LearningUnit learningUnit) {
        super(name, position, learningUnit);
    }
}