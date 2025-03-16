package ch.nova_omnia.lernello.model.data;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "learning_units")
public class LearningUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    @NotNull
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "learning_kit_id", nullable = false)
    private LearningKit learningKit;

    @Column(name = "name", nullable = false)
    @NotNull
    @NotBlank
    private String name;

    public LearningUnit() {
    }

    public LearningUnit(@NotNull @NotBlank String name, @NotNull LearningKit learningKit) {
        this.name = name;
        this.learningKit = learningKit;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull @NotBlank String name) {
        this.name = name;
    }

    public LearningKit getLearningKit() {
        return learningKit;
    }

    public void setLearningKit(@NotNull LearningKit learningKit) {
        this.learningKit = learningKit;
    }

}
