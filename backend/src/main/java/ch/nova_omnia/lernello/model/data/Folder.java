package ch.nova_omnia.lernello.model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "folders")
@Data
@NoArgsConstructor
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    @NotNull
    private UUID uuid;

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 3, max = 40)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_folder_id")
    private Folder parentFolder;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> subFolders = new ArrayList<>();

    public Folder(String name) {
        this.name = name;
    }

    public Folder(String name, Folder parentFolder) {
        this.name = name;
        setParentFolder(parentFolder);
    }

    public void setParentFolder(Folder parentFolder) {
        if (parentFolder != null && isCircularReference(parentFolder)) {
            throw new IllegalArgumentException("Circular reference detected: A folder cannot be its own ancestor.");
        }
        this.parentFolder = parentFolder;
    }

    private boolean isCircularReference(Folder parentFolder) {
        Folder current = parentFolder;
        while (current != null) {
            if (current.equals(this)) {
                return true;
            }
            current = current.getParentFolder();
        }
        return false;
    }
}
