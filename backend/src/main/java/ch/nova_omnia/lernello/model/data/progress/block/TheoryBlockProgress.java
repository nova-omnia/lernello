package ch.nova_omnia.lernello.model.data.progress.block;

import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.model.data.user.User;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TheoryBlockProgress extends BlockProgress {

    public TheoryBlockProgress(User user, TheoryBlock block) {
        super(user, block);
    }
}
