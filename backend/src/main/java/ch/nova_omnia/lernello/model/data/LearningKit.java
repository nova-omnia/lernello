package ch.nova_omnia.lernello.model.data;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "learning_kits")
public class LearningKit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    @NotNull
    private UUID uuid;

    @Column(name = "name", nullable = false)
    @NotNull
    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = false)
    @NotNull
    private Folder folder;

    @OneToMany(mappedBy = "learningKit", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private List<LearningUnit> learningUnits;

    public LearningKit() {
    }

    public LearningKit(@NotNull @NotBlank String name, @NotNull Folder folder, @NotNull List<LearningUnit> learningUnits) {
        this.name = name;
        this.folder = folder;
        this.learningUnits = learningUnits;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull @NotBlank String name) {
        this.name = name;
    }

    public Folder getParentFolder() {
        return folder;
    }

    public void setParentFolder(@NotNull Folder folder) {
        this.folder = folder;
    }

    public List<LearningUnit> getLearningUnits() {
        return learningUnits;
    }

    public void setLearningUnits(@NotNull List<LearningUnit> learningUnits) {
        this.learningUnits = learningUnits;
    }
}
