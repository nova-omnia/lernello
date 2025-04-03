package ch.nova_omnia.lernello.model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.model.data.block.Block;
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

@Data
@Entity
@Table(name = "learning_units")
@NoArgsConstructor
public class LearningUnit {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "learning_kit_id", nullable = false)
    private LearningKit learningKit;

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 3, max = 40)
    private String name;

    @OneToMany(mappedBy = "learningUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Block> blocks = new ArrayList<>();

    public LearningUnit(String name, LearningKit learningKit) {
        this.name = name;
        this.learningKit = learningKit;
    }
}