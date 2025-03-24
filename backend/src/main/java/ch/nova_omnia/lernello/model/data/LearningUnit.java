package ch.nova_omnia.lernello.model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.model.data.blocks.Block;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
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
    @NotNull
    @NotBlank
    @Size(min = 3, max = 40)
    private String name;

    @OneToMany(mappedBy = "learningUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Block> blocks = new ArrayList<>();

    public LearningUnit(String name, LearningKit learningKit) {
        this.name = name;
        this.learningKit = learningKit;
    }

    public void addBlock(Block block) {
        blocks.add(block);
        block.setLearningUnit(this);
    }

    public void removeBlock(Block block) {
        blocks.remove(block);
        block.setLearningUnit(null);
    }
}