package ch.nova_omnia.lernello.model.data.block;

import java.util.UUID;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "blocks")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
public abstract class Block {

    @NotNull
    @Column(name = "type", nullable = false)
    private BlockType type;

    @Id
    @Column(name = "id", nullable = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Lob
    @NotBlank
    @Column(name = "name")
    @Size(min = 3, max = 100)
    private String name;

    @Min(0)
    @Column(name = "position")
    private int position;

    @ManyToOne
    @JoinColumn(name = "learning_unit_id")
    private LearningUnit learningUnit;

    protected Block(BlockType type, String name, int position, LearningUnit learningUnit) {
        this.type = type;
        this.name = name;
        this.position = position;
        this.learningUnit = learningUnit;
    }
}

