package ch.nova_omnia.lernello.model.data.progress.block.scorable;

import java.util.ArrayList;
import java.util.List;

import ch.nova_omnia.lernello.model.data.block.scorable.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.progress.LearningUnitProgress;
import ch.nova_omnia.lernello.model.data.user.User;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DiscriminatorValue("MULTIPLE_CHOICE")
public class MultipleChoiceBlockProgress extends ScorableBlockProgress {

    @ElementCollection
    @CollectionTable(name = "multiple_choice_last_answers", joinColumns = @JoinColumn(name = "block_id"))
    @Column(name = "last_answer", nullable = true)
    private List<String> lastAnswers = new ArrayList<>();

    public MultipleChoiceBlockProgress(User user, MultipleChoiceBlock block, LearningUnitProgress unitProgress, List<String> lastAnswers, Boolean isCorrect, Integer scoreReached) {
        super(user, block, unitProgress, scoreReached, isCorrect);
        this.lastAnswers = lastAnswers;
    }

    public MultipleChoiceBlockProgress(User user, MultipleChoiceBlock block, LearningUnitProgress unitProgress, List<String> lastAnswers, Boolean isCorrect) {
        super(user, block, unitProgress, isCorrect);
        this.lastAnswers = lastAnswers;
    }

    public MultipleChoiceBlockProgress(User user, MultipleChoiceBlock block, LearningUnitProgress unitProgress) {
        super(user, block, unitProgress);
    }

}
