package ch.nova_omnia.pm4.model.data;

import jakarta.persistence.*;

@Entity
@Table(name = "learning_units")
public class LearningUnit extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "learning_kit_id", nullable = false)
    private LearningKit learningKit;

    public LearningKit getLearningKit() {
        return learningKit;
    }

    public void setLearningKit(LearningKit learningKit) {
        this.learningKit = learningKit;
    }

    public Long getLearningKitId() {
        return learningKit.getId();
    }
}
