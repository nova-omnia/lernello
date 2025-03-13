package ch.nova_omnia.pm4.model.data;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "learning_kits")
public class LearningKit extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "parent_folder_id", nullable = false)
    private Folder parentFolder;

    @OneToMany(mappedBy = "learningKit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearningUnit> learningUnits;

    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public Long getParentFolderId() {
        return parentFolder != null ? parentFolder.getId() : null;
    }

    public List<LearningUnit> getLearningUnits() {
        return learningUnits;
    }

    public void setLearningUnits(List<LearningUnit> learningUnits) {
        this.learningUnits = learningUnits;
    }
}
