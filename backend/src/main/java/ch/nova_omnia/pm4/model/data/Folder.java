package ch.nova_omnia.pm4.model.data;

import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "folders")
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

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

    public FolderDTO toDTO(int depth) {
        FolderDTO dto = new FolderDTO();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setInstructor(this.instructor.getUsername());
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