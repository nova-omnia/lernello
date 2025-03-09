package ch.nova_omnia.pm4.service;

import ch.nova_omnia.pm4.model.data.Folder;
import ch.nova_omnia.pm4.repository.FolderRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * FolderService is a service class that provides business logic for managing {@link Folder} entities.
 */
@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    /**
     * Constructs a FolderService with the specified {@link FolderRepository}.
     * 
     * @param folderRepository the repository to use for managing Folder entities
     */
    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    /**
     * Retrieves a folder by its ID.
     * 
     * @param id the ID of the folder to retrieve
     * @return the folder with the specified ID
     * @throws RuntimeException if the folder is not found
     */
    public Folder getFolderById(Long id) {
        return folderRepository.findById(id).orElseThrow(() -> new RuntimeException("Folder not found"));
    }

    /**
     * Creates a new folder.
     * 
     * @param folder the folder to create
     * @return the created folder
     */
    public Folder createFolder(Folder folder) {
        return folderRepository.save(folder);
    }

    /**
     * Updates an existing folder.
     * 
     * @param id the ID of the folder to update
     * @param folder the folder with updated information
     * @return the updated folder
     * @throws RuntimeException if the folder is not found
     */
    public Folder updateFolder(Long id, Folder folder) {
        Folder existingFolder = folderRepository.findById(id).orElseThrow(() -> new RuntimeException("Folder not found"));
        existingFolder.setName(folder.getName());
        existingFolder.setInstructor(folder.getInstructor());
        existingFolder.setParentFolder(folder.getParentFolder());
        existingFolder.setSubFolders(folder.getSubFolders());
        existingFolder.setLearningUnits(folder.getLearningUnits());
        return folderRepository.save(existingFolder);
    }

    /**
     * Deletes a folder by its ID.
     * 
     * @param id the ID of the folder to delete
     * @throws RuntimeException if the folder is not found
     */
    public void deleteFolder(Long id) {
        Folder existingFolder = folderRepository.findById(id).orElseThrow(() -> new RuntimeException("Folder not found"));
        folderRepository.delete(existingFolder);
    }

    /**
     * Sets the parent folder for a specified folder.
     * 
     * @param folderId the ID of the folder to update
     * @param parentFolderId the ID of the parent folder to set
     * @return the updated folder
     * @throws RuntimeException if the folder or parent folder is not found
     */
    public Folder setParentFolder(Long folderId, Long parentFolderId) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new RuntimeException("Folder not found"));
        Folder parentFolder = folderRepository.findById(parentFolderId).orElseThrow(() -> new RuntimeException("Parent folder not found"));
        folder.setParentFolder(parentFolder);
        return folderRepository.save(folder);
    }

    public List<Folder> getAllFolders() {
        return (List<Folder>) folderRepository.findAll();
    }
}