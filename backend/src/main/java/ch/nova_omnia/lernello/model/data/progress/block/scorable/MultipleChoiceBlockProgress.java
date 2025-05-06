package ch.nova_omnia.lernello.model.data.progress.block.scorable;

import ch.nova_omnia.lernello.model.data.block.scorable.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MultipleChoiceBlockProgress extends ScorableBlockProgress {

    @ElementCollection
    @CollectionTable(name = "multiple_choice_last_answers", joinColumns = @JoinColumn(name = "block_id"))
    @Column(name = "last_answer")
    @NotEmpty
    private List<String> lastAnswers = new ArrayList<>();

    public MultipleChoiceBlockProgress(User user, MultipleChoiceBlock block, List<String> lastAnswers, int scoreReached) {
        super(user, block, scoreReached);
        this.lastAnswers = lastAnswers;
    }

    public MultipleChoiceBlockProgress(User user, MultipleChoiceBlock block, List<String> lastAnswers) {
        super(user, block);
        this.lastAnswers = lastAnswers;
    }
}
