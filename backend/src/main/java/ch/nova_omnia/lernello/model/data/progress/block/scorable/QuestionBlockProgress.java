package ch.nova_omnia.lernello.model.data.progress.block.scorable;

import ch.nova_omnia.lernello.model.data.block.scorable.QuestionBlock;
import ch.nova_omnia.lernello.model.data.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class QuestionBlockProgress extends ScorableBlockProgress {

    @Column(name = "score_reached", nullable = true)
    private Integer scoreReached;

    @Column(name = "last_answer", columnDefinition = "TEXT", nullable = false)
    private String lastAnswer;

    public QuestionBlockProgress(User user, QuestionBlock block, String lastAnswer) {
        super(user, block);
        this.lastAnswer = lastAnswer;
    }
}
