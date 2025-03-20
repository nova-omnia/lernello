package ch.nova_omnia.lernello.model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.model.data.blocks.Block;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "learning_units")
public class LearningUnit {
    @NotNull
    @NotBlank
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "learning_kit_id", nullable = false)
    private LearningKit learningKit;

    @Column(name = "name", nullable = false)
    @NotNull
    @Size(min = 2, max = 32)
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "learningUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Block> blocks = new ArrayList<>();

    public LearningUnit() {
    }

    public LearningUnit(String name, LearningKit learningKit) {
        this.name = name;
        this.learningKit = learningKit;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LearningKit getLearningKit() {
        return learningKit;
    }

    public void setLearningKit(LearningKit learningKit) {
        this.learningKit = learningKit;
    }

    public List<Block> getBlocks() {
        return blocks;
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
