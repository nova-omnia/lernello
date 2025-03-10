package ch.nova_omnia.pm4.model.data;

import jakarta.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearningUnit> learningUnits;

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public List<Folder> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(List<Folder> subFolders) {
        this.subFolders = subFolders;
    }

    public List<LearningUnit> getLearningUnits() {
        return learningUnits;
    }

    public void setLearningUnits(List<LearningUnit> learningUnits) {
        this.learningUnits = learningUnits;
    }

}