package ch.nova_omnia.pm4.model.data;

import jakarta.persistence.*;

/**
 * LearningUnit is an entity class that represents a learning unit in the system.
 * It extends {@link AbstractEntity} and contains additional properties and methods
 * specific to learning units.
 */
@Entity
@Table(name = "learning_units")
public class LearningUnit extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;

    /**
     * Returns the folder associated with the learning unit.
     * 
     * @return the folder
     */
    public Folder getFolder() {
        return folder;
    }

    /**
     * Sets the folder associated with the learning unit.
     * 
     * @param folder the folder to set
     */
    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    /**
     * Converts this learning unit entity to a {@link LearningUnitDTO} object.
     * 
     * @return the learning unit DTO
     */
    public LearningUnitDTO toDTO() {
        LearningUnitDTO dto = new LearningUnitDTO();
        dto.setId(this.getId());
        dto.setName(this.getName());
        return dto;
    }
}
