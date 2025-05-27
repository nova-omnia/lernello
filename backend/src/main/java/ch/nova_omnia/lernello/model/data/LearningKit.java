package ch.nova_omnia.lernello.model.data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import ch.nova_omnia.lernello.model.data.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "learning_kits")
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@ToString(exclude = {"learningUnits", "trainees", "files"
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class LearningKit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    @NotNull
    @EqualsAndHashCode.Include
    private UUID uuid;

    @Column(name = "name", nullable = false)
    @NotBlank
    @NonNull
    @Size(min = 3, max = 40)
    private String name;

    @Column(name = "description")
    @Size(max = 200)
    private String description;

    @Column(name = "deadlineDate")
    private ZonedDateTime deadlineDate;

    @Column(name = "published")
    private boolean published;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "learningKit", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<LearningUnit> learningUnits = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "learning_kit_trainees", joinColumns = @JoinColumn(name = "learning_kit_id"), inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @OrderBy("surname ASC")
    private List<User> trainees = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderBy("name ASC")
    private List<File> files = new ArrayList<>();

    @Column(name = "averageProgress", nullable = false)
    @Min(0)
    @Max(100)
    private int averageProgress = 0;

    @Column(name = "completionRate", nullable = false)
    @Min(0)
    @Max(100)
    private int completionRate = 0;
}
