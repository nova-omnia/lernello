package ch.nova_omnia.lernello.model.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "learning_kits")
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
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
    private Date deadlineDate;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Column(name = "context")
    private String context;

    @OneToMany(mappedBy = "learningKit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearningUnit> learningUnits = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "learning_kit_participants",
            joinColumns = @JoinColumn(name = "learning_kit_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants = new ArrayList<>();

    @OneToMany(mappedBy = "learningKit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files = new ArrayList<>();

    // For testing purposes
    public LearningKit(String name, Folder folder) {
        this.name = name;
        this.folder = folder;
    }
}
