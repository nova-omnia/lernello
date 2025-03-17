package ch.nova_omnia.pm4.model.learningUnit;

import ch.nova_omnia.pm4.model.learningUnit.blocks.Block;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "learning_units")
public class LearningUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // A Learning Unit can have multiple blocks
    @OneToMany(mappedBy = "learningUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Block> blocks = new ArrayList<>();

    public LearningUnit() {}

    public LearningUnit(String name) {
        this.name = name;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setName(String name) {
        this.name = name;
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
