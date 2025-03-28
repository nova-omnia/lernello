package ch.nova_omnia.lernello.model.data;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;

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

    @Column(name = "description", nullable = true)
    @Size(max = 500)
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "deadline", nullable = true)
    private Date deadline;

    @Column(name = "language", nullable = false)
    @NotBlank
    @Size(min = 2, max = 2) //then we would parse it/ handle it in backend like "en", "de", "fr", "it"
    private String language;

    @Column(name = "context", nullable = true)
    @Size(max = 500)
    private String context;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @OneToMany(mappedBy = "learningKit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearningUnit> learningUnits = new ArrayList<>();

    public LearningKit(String name, Folder folder) {
        this.name = name;
        this.folder = folder;
    }

    public LearningKit(String name, Folder folder, String description, Date deadline, String language, String context) {
        this.name = name;
        this.folder = folder;
        this.description = description;
        this.deadline = deadline;
        this.language = language;
        this.context = context;
    }


}
