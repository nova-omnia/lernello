package ch.nova_omnia.pm4.model.data;

import java.util.List;

/**
 * FolderDTO is a Data Transfer Object (DTO) that represents a folder in the system.
 * It extends {@link AbstractDTO} and contains additional properties and methods
 * specific to folders.
 */
public class FolderDTO extends AbstractDTO {
    private String instructor;
    private Long parentFolder;
    private List<FolderDTO> subFolders;
    private List<LearningUnitDTO> learningUnits;

    /**
     * Returns the instructor associated with the folder DTO.
     * 
     * @return the instructor
     */
    public String getInstructor() {
        return instructor;
    }

    /**
     * Sets the instructor associated with the folder DTO.
     * 
     * @param instructor the instructor to set
     */
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    /**
     * Returns the parent folder ID of this folder DTO.
     * 
     * @return the parent folder ID
     */
    public Long getParentFolder() {
        return parentFolder;
    }

    /**
     * Sets the parent folder ID of this folder DTO.
     * 
     * @param parentFolder the parent folder ID to set
     */
    public void setParentFolder(Long parentFolder) {
        this.parentFolder = parentFolder;
    }

    /**
     * Returns the list of subfolder DTOs contained within this folder DTO.
     * 
     * @return the list of subfolder DTOs
     */
    public List<FolderDTO> getSubFolders() {
        return subFolders;
    }

    /**
     * Sets the list of subfolder DTOs contained within this folder DTO.
     * 
     * @param subFolders the list of subfolder DTOs to set
     */
    public void setSubFolders(List<FolderDTO> subFolders) {
        this.subFolders = subFolders;
    }

    /**
     * Returns the list of learning unit DTOs contained within this folder DTO.
     * 
     * @return the list of learning unit DTOs
     */
    public List<LearningUnitDTO> getLearningUnits() {
        return learningUnits;
    }

    /**
     * Sets the list of learning unit DTOs contained within this folder DTO.
     * 
     * @param learningUnits the list of learning unit DTOs to set
     */
    public void setLearningUnits(List<LearningUnitDTO> learningUnits) {
        this.learningUnits = learningUnits;
    }
}