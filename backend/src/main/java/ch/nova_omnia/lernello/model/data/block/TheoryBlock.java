package ch.nova_omnia.lernello.model.data.block;

import static ch.nova_omnia.lernello.model.data.block.BlockType.THEORY;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TheoryBlock extends Block {

    @Lob
    @Column(name = "content", nullable = true)
    private String content;

    public TheoryBlock(String name, int position, LearningUnit learningUnit, String content) {
        super(THEORY, name, position, learningUnit);
        this.content = content;
    }
}
