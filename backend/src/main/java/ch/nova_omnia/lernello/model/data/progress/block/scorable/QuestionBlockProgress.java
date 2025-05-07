package ch.nova_omnia.lernello.model.data.progress.block.scorable;

import ch.nova_omnia.lernello.model.data.block.scorable.QuestionBlock;
import ch.nova_omnia.lernello.model.data.progress.LearningUnitProgress;
import ch.nova_omnia.lernello.model.data.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DiscriminatorValue("QUESTION")
public class QuestionBlockProgress extends ScorableBlockProgress {

    @Column(name = "score_reached", nullable = true)
    private Integer scoreReached;

    @Column(name = "last_answer", columnDefinition = "TEXT", nullable = false)
    private String lastAnswer;

    public QuestionBlockProgress(User user, QuestionBlock block, String lastAnswer, LearningUnitProgress unitProgress) {
        super(user, block, unitProgress);
        this.lastAnswer = lastAnswer;
    }

    public QuestionBlockProgress(User user, QuestionBlock block, Integer scoreReached, String lastAnswer, LearningUnitProgress unitProgress) {
        super(user, block, unitProgress);
        this.scoreReached = scoreReached;
        this.lastAnswer = lastAnswer;
    }

    public QuestionBlockProgress(User user, QuestionBlock block, LearningUnitProgress unitProgress) {
        super(user, block, unitProgress);
    }
}
