package ch.nova_omnia.lernello.model.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "learning_kits")
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LearningKit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    @NotNull
    private UUID uuid;

    @Column(name = "name", nullable = false)
    @NotBlank
    @NonNull
    @Size(min = 3, max = 40)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "deadlineDate")
    private ZonedDateTime deadlineDate;


    @Column(name = "context")
    private String context;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "learningKit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearningUnit> learningUnits = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "learning_kit_participants", joinColumns = @JoinColumn(name = "learning_kit_id"), inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private List<File> files = new ArrayList<>();
}
