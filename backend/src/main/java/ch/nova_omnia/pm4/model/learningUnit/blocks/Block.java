package ch.nova_omnia.pm4.model.learningUnit.blocks;

import ch.nova_omnia.pm4.model.learningUnit.LearningUnit;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "block_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int position;

    @ManyToOne
    @JoinColumn(name = "learning_unit_id")
    private LearningUnit learningUnit;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public LearningUnit getLearningUnit() {
        return learningUnit;
    }

    public void setLearningUnit(LearningUnit learningUnit) {
        this.learningUnit = learningUnit;
    }
}

