package ch.nova_omnia.lernello.model.data.progress.block.scorable;

import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.progress.LearningUnitProgress;
import ch.nova_omnia.lernello.model.data.progress.block.BlockProgress;
import ch.nova_omnia.lernello.model.data.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class ScorableBlockProgress extends BlockProgress {

    @Column(name = "score_reached", nullable = true)
    private Integer scoreReached;

    public ScorableBlockProgress(User user, Block block, LearningUnitProgress learningUnitProgress, Integer scoreReached) {
        super(user, block, learningUnitProgress);
        this.scoreReached = scoreReached;
    }

    public ScorableBlockProgress(User user, Block block, LearningUnitProgress learningUnitProgress) {
        super(user, block, learningUnitProgress);
    }
}
