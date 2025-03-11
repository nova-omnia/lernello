package ch.nova_omnia.pm4.model.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Instructor is an entity class that represents an instructor in the system.
 * It extends {@link AbstractEntity} and contains additional properties and methods
 * specific to instructors.
 */
@Entity
@Table(name = "instructors")
@JsonIgnoreProperties({"folders"})
public class Instructor extends AbstractEntity {

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> folders;

    /**
     * Returns the list of folders associated with the instructor.
     * 
     * @return the list of folders
     */
    public List<Folder> getFolders() {
        return folders;
    }

    /**
     * Sets the list of folders associated with the instructor.
     * 
     * @param folders the list of folders to set
     */
    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }
}
