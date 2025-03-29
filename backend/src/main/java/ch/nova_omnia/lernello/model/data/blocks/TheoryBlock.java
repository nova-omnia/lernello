package ch.nova_omnia.lernello.model.data.blocks;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class TheoryBlock extends Block {

    @Lob
    @Column(name = "content")
    private String content;

    public TheoryBlock(String name, int position,String blockType, LearningUnit learningUnit, String content) {
        super(name, position, blockType, learningUnit);
        this.content = content;
    }
}