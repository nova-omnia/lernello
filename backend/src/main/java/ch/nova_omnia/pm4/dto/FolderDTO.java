package ch.nova_omnia.pm4.dto;

import java.util.List;

public class FolderDto extends BaseDto {
    private BaseDto parent;
    private List<FolderDto> subFolders;
    private List<LearningKitDto> learningKits;
    
    public BaseDto getParent() {
        return parent;
    }
    public void setParent(BaseDto parent) {
        this.parent = parent;
    }
    public List<FolderDto> getSubFolders() {
        return subFolders;
    }
    public void setSubFolders(List<FolderDto> subFolders) {
        this.subFolders = subFolders;
    }
    public List<LearningKitDto> getLearningKits() {
        return learningKits;
    }
    public void setLearningKits(List<LearningKitDto> learningKits) {
        this.learningKits = learningKits;
    }
}
