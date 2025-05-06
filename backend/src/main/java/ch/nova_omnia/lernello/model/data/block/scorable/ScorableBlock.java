package ch.nova_omnia.lernello.model.data.block.scorable;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.BlockType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class ScorableBlock extends Block {

    @Column(name = "max_score", nullable = true)
    private int maxScore;

    public ScorableBlock(BlockType type, String name, int position, LearningUnit learningUnit) {
        super(type, name, position, learningUnit);
    }

    public ScorableBlock(BlockType type, String name, int position, LearningUnit learningUnit, int maxScore) {
        super(type, name, position, learningUnit);
        this.maxScore = maxScore;
    }
}
