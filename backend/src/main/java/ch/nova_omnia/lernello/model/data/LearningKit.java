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
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "learning_kits")
@Data
@NoArgsConstructor
public class LearningKit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    @NotNull
    private UUID uuid;

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 3, max = 40)
    private String name;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @OneToMany(mappedBy = "learningKit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearningUnit> learningUnits;

    public LearningKit(String name, Folder folder) {
        this.name = name;
        this.folder = folder;
    }


}
