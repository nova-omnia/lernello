package ch.nova_omnia.lernello.model.data.progress.block;

import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
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
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("THEORY")
public class TheoryBlockProgress extends BlockProgress {

    @Column(name = "is_viewed")
    private Boolean isViewed = false;

    public TheoryBlockProgress(User user, TheoryBlock block, LearningUnitProgress unitProgress) {
        super(user, block, unitProgress);
    }

    public TheoryBlockProgress(User user, TheoryBlock block, Boolean isViewed, LearningUnitProgress unitProgress) {
        super(user, block, unitProgress);
        this.isViewed = isViewed;
    }
}
