package ch.nova_omnia.lernello.model.data.blocks;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "blocks")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "block_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Block {

    @Id
    @Column(name = "id", nullable = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Min(0)
    @Column(name = "position")
    private int position;

    @NotBlank
    @Column(name = "name")
    @Size(min = 3, max = 40)
    private String name;

    @ManyToOne
    @JoinColumn(name = "learning_unit_id")
    private LearningUnit learningUnit;

    protected Block(String name, int position, LearningUnit learningUnit) {
        this.name = name;
        this.position = position;
        this.learningUnit = learningUnit;
    }
}
