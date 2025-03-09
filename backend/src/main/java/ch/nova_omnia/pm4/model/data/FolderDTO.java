package ch.nova_omnia.pm4.model.data;

import java.util.List;

public class FolderDTO {
    private Long id;
    private String name;
    private String instructor;
    private Long parentFolder;
    private List<FolderDTO> subFolders;
    private List<LearningUnitDTO> learningUnits;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Long getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Long parentFolder) {
        this.parentFolder = parentFolder;
    }

    public List<FolderDTO> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(List<FolderDTO> subFolders) {
        this.subFolders = subFolders;
    }

    public List<LearningUnitDTO> getLearningUnits() {
        return learningUnits;
    }

    public void setLearningUnits(List<LearningUnitDTO> learningUnits) {
        this.learningUnits = learningUnits;
    }
}