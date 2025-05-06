package ch.nova_omnia.lernello.model.data.progress.block;

import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.progress.LearningUnitProgress;
import ch.nova_omnia.lernello.model.data.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "user_block_progress")
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class BlockProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "block_id", nullable = false)
    private Block block;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "learning_unit_progress_id", nullable = false)
    private LearningUnitProgress learningUnitProgress;

    public BlockProgress(User user, Block block) {
        this.user = user;
        this.block = block;
    }
}
