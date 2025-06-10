package ch.nova_omnia.lernello.model.data.progress;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "user_learning_kit_progress")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LearningKitProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_learning_kit_progress_user", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(uuid) ON DELETE CASCADE"))
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "learning_kit_id", nullable = false)
    private LearningKit learningKit;

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
            name = "progress_percentage", nullable = false, columnDefinition = "INT CHECK (progress_percentage BETWEEN 0 AND 100)"
    )
    private int progressPercentage = 0;

    @OneToMany(mappedBy = "learningKitProgress", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<LearningUnitProgress> learningUnitProgresses = new ArrayList<>();

    public LearningKitProgress(User user, LearningKit learningKit) {
        this.user = user;
        this.learningKit = learningKit;
    }

    public LearningKitProgress(User user, LearningKit learningKit, List<LearningUnitProgress> learningUnitProgresses) {
        this.user = user;
        this.learningKit = learningKit;
        this.learningUnitProgresses = learningUnitProgresses;
    }

    public void addLearningUnitProgress(LearningUnitProgress learningUnitProgress) {
        learningUnitProgresses.add(learningUnitProgress);
        learningUnitProgress.setLearningKitProgress(this);
    }

    public void removeLearningUnitProgress(LearningUnitProgress learningUnitProgress) {
        learningUnitProgresses.remove(learningUnitProgress);
        learningUnitProgress.setLearningKitProgress(null);
    }
}
