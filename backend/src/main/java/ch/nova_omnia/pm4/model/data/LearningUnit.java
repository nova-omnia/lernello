package ch.nova_omnia.pm4.model.data;

import jakarta.persistence.*;

@Entity
@Table(name = "learning_units")
public class LearningUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public LearningUnitDTO toDTO() {
        LearningUnitDTO dto = new LearningUnitDTO();
        dto.setId(this.id);
        dto.setTitle(this.title);
        return dto;
    }
}
