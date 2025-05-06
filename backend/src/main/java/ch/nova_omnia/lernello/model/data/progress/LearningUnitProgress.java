package ch.nova_omnia.lernello.model.data.progress;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.progress.block.BlockProgress;
import ch.nova_omnia.lernello.model.data.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
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
    @OneToMany(mappedBy = "userLearningUnitProgress", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BlockProgress> userBlockProgresses = new ArrayList<>();

    @Column(name = "opened", nullable = false)
    private boolean opened = false;

    @Column(name = "completed", nullable = false)
    private boolean completed = false;

    @CreatedDate
    @Column(name = "first_opened_at", nullable = false, updatable = false)
    private LocalDateTime firstOpenedAt;

    @LastModifiedDate
    @Column(name = "last_opened_at")
    private LocalDateTime lastOpenedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

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
            this.firstOpenedAt = LocalDateTime.now();
        }
        this.lastOpenedAt = LocalDateTime.now();
    }

    public void markAsCompleted() {
        this.completed = true;
        this.completedAt = LocalDateTime.now();
        this.lastOpenedAt = LocalDateTime.now();
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
