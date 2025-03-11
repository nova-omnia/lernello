package ch.nova_omnia.pm4.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

/**
 * LearningKit is an entity class that represents a learning unit in the system.
 * It extends {@link AbstractEntity} and contains additional properties and methods
 * specific to learning units.
 */
@Entity
@Table(name = "learning_kits")
public class LearningKit extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "parent_folder_id", nullable = false)
    @JsonIgnore
    private Folder parentFolder;

    @Transient
    private Long parentFolderId;

    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }
}
