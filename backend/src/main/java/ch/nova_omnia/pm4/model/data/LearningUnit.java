package ch.nova_omnia.pm4.model.data;

import jakarta.persistence.*;

@Entity
@Table(name = "learning_units")
public class LearningUnit extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "parent_learning_kit_id", nullable = false)
    private LearningKit parentLearningKit;

    @Transient
    private Long parentLearningKitId;

    public LearningKit getParentLearningKit() {
        return parentLearningKit;
    }

    public void setParentLearningKit(LearningKit parentLearningKit) {
        this.parentLearningKit = parentLearningKit;
    }
}