package ch.nova_omnia.lernello.model.data.progress;

import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_block_progress")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserBlockProgress {

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

    @Column(name = "completed", nullable = false)
    private boolean completed = false;

    @Column(name = "score")
    private Integer score; // Optional: if blocks can be scored

    @Column(name = "last_answer", columnDefinition = "TEXT")
    private String lastAnswer; // Optional: for question blocks

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public UserBlockProgress(User user, Block block) {
        this.user = user;
        this.block = block;
    }
}
