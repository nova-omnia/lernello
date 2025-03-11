package ch.nova_omnia.pm4.dto;

public class LearningUnitDTO extends AbstractDTO {
    private Long learningKitId;

    public Long getLearningKitId() {
        return learningKitId;
    }

    public void setLearningKitId(Long learningKitId) {
        this.learningKitId = learningKitId;
    }
}