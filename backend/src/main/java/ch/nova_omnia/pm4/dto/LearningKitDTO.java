package ch.nova_omnia.pm4.dto;

import java.util.List;

public class LearningKitDto extends BaseDto {
    private BaseDto parent;
    private List<LearningUnitDto> learningUnits;

    public BaseDto getParent() {
        return parent;
    }

    public void setParent(BaseDto parent) {
        this.parent = parent;
    }

    public List<LearningUnitDto> getLearningUnits() {
        return learningUnits;
    }

    public void setLearningUnits(List<LearningUnitDto> learningUnits) {
        this.learningUnits = learningUnits;
    }
}
