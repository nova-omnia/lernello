package ch.nova_omnia.pm4.model.data;

import jakarta.persistence.*;

@Entity
@Table(name = "learning_kits")
public class LearningKit extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "parent_folder_id", nullable = false)
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
