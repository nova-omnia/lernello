package ch.nova_omnia.lernello.model.data.blocks;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("theory_block")
@NoArgsConstructor
public class TheoryBlock extends Block {

    @Lob
    @Column(name = "content")
    private String content;
    
    public TheoryBlock(String name, int position, String content, LearningUnit learningUnit) {
        super(name, position, learningUnit);
        this.content = content;
    }
}