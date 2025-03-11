package ch.nova_omnia.pm4.dto;

public class LearningUnitDto extends BaseDto {
    private BaseDto parent;

    public BaseDto getParent() {
        return parent;
    }

    public void setParent(BaseDto parent) {
        this.parent = parent;
    }
}
