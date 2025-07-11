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
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "learning_units")
@ToString(exclude = "learningKit")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class LearningUnit {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    @EqualsAndHashCode.Include
    private UUID uuid;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "learning_kit_id", nullable = false)
    private LearningKit learningKit;

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 3, max = 40)
    private String name;

    @Min(0)
    @Column(name = "position", nullable = false)
    private int position;

    @OneToMany(mappedBy = "learningUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<Block> blocks = new ArrayList<>();

    public LearningUnit(String name, LearningKit learningKit) {
        this.name = name;
        this.learningKit = learningKit;
    }

}
