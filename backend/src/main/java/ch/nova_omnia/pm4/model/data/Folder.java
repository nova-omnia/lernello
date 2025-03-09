package ch.nova_omnia.pm4.model.data;

import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Folder is an entity class that represents a folder in the system.
 * It extends {@link AbstractEntity} and contains additional properties
 * and methods specific to folders.
 */
@Entity
@Table(name = "folders")
public class Folder extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @ManyToOne
    @JoinColumn(name = "parent_folder_id")
    private Folder parentFolder;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> subFolders;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearningUnit> learningUnits;

    /**
     * Returns the instructor associated with the folder.
     * 
     * @return the instructor
     */
    public Instructor getInstructor() {
        return instructor;
    }

    /**
     * Sets the instructor associated with the folder.
     * 
     * @param instructor the instructor to set
     */
    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    /**
     * Returns the parent folder of this folder.
     * 
     * @return the parent folder
     */
    public Folder getParentFolder() {
        return parentFolder;
    }

    /**
     * Sets the parent folder of this folder.
     * 
     * @param parentFolder the parent folder to set
     */
    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

    /**
     * Returns the list of subfolders contained within this folder.
     * 
     * @return the list of subfolders
     */
    public List<Folder> getSubFolders() {
        return subFolders;
    }

    /**
     * Sets the list of subfolders contained within this folder.
     * 
     * @param subFolders the list of subfolders to set
     */
    public void setSubFolders(List<Folder> subFolders) {
        this.subFolders = subFolders;
    }

    /**
     * Returns the list of learning units contained within this folder.
     * 
     * @return the list of learning units
     */
    public List<LearningUnit> getLearningUnits() {
        return learningUnits;
    }

    /**
     * Sets the list of learning units contained within this folder.
     * 
     * @param learningUnits the list of learning units to set
     */
    public void setLearningUnits(List<LearningUnit> learningUnits) {
        this.learningUnits = learningUnits;
    }

    /**
     * Converts this folder entity to a {@link FolderDTO} object.
     * 
     * @param depth the depth of subfolder and learning unit conversion
     * @return the folder DTO
     */
    public FolderDTO toDTO(int depth) {
        FolderDTO dto = new FolderDTO();
        dto.setId(this.getId());
        dto.setName(this.getName());
        dto.setInstructor(this.instructor.getName());
        dto.setParentFolder(this.parentFolder != null ? this.parentFolder.getId() : null);
        if (depth > 0) {
            dto.setSubFolders(this.subFolders.stream()
                .map(subFolder -> subFolder.toDTO(depth - 1))
                .collect(Collectors.toList()));
            dto.setLearningUnits(this.learningUnits.stream()
                .map(LearningUnit::toDTO)
                .collect(Collectors.toList()));
        }
        return dto;
    }
}