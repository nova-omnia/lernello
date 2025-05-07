package ch.nova_omnia.lernello.model.data.progress;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.progress.block.BlockProgress;
import ch.nova_omnia.lernello.model.data.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_learning_unit_progress")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LearningUnitProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_unit_id", nullable = false)
    private LearningUnit learningUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_kit_progress_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private LearningKitProgress learningKitProgress;

    @NotNull
    @OneToMany(mappedBy = "learningUnitProgress", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BlockProgress> userBlockProgresses = new ArrayList<>();

    @Column(name = "opened", nullable = false)
    private boolean opened = false;

    @Column(name = "completed", nullable = false)
    private boolean completed = false;

    @CreatedDate
    @Column(name = "first_opened_at", nullable = false, updatable = false)
    private ZonedDateTime firstOpenedAt;

    @LastModifiedDate
    @Column(name = "last_opened_at")
    private ZonedDateTime lastOpenedAt;

    @Column(name = "completed_at")
    private ZonedDateTime completedAt;

    @Min(0)
    @Max(100)
    @Column(
        name = "progress_percentage",
        nullable = false,
        columnDefinition = "INT CHECK (progress_percentage BETWEEN 0 AND 100)"
    )
    private int progressPercentage = 0;

    public LearningUnitProgress(User user, LearningUnit learningUnit) {
        this.user = user;
        this.learningUnit = learningUnit;
    }

    public LearningUnitProgress(User user, LearningUnit learningUnit, List<BlockProgress> userBlockProgresses) {
        this.user = user;
        this.learningUnit = learningUnit;
        this.userBlockProgresses = userBlockProgresses;
    }

    public void markAsOpened() {
        this.opened = true;
        if (this.firstOpenedAt == null) {
            this.firstOpenedAt = ZonedDateTime.now();
        }
        this.lastOpenedAt = ZonedDateTime.now();
    }

    public void markAsCompleted() {
        this.completed = true;
        this.completedAt = ZonedDateTime.now();
        this.lastOpenedAt = ZonedDateTime.now();
    }

    public void addBlockProgress(BlockProgress blockProgress) {
        userBlockProgresses.add(blockProgress);
        blockProgress.setLearningUnitProgress(this);
    }

    public void removeBlockProgress(BlockProgress blockProgress) {
        userBlockProgresses.remove(blockProgress);
        blockProgress.setLearningUnitProgress(null);
    }
}
