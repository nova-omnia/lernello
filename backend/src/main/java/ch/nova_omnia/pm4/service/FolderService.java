package ch.nova_omnia.pm4.service;

import ch.nova_omnia.pm4.model.data.Folder;
import ch.nova_omnia.pm4.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public List<Folder> getAllFolders() {
        Iterable<Folder> folders = folderRepository.findAll();
        return StreamSupport.stream(folders.spliterator(), false)
                            .collect(Collectors.toList());
    }

    public Folder getFolderById(Long id) {
        return folderRepository.findById(id).orElseThrow(() -> new RuntimeException("Folder not found"));
    }

    public Folder createFolder(Folder folder) {
        return folderRepository.save(folder);
    }

    public Folder updateFolder(Long id, Folder folder) {
        Folder existingFolder = folderRepository.findById(id).orElseThrow(() -> new RuntimeException("Folder not found"));
        existingFolder.setName(folder.getName());
        return folderRepository.save(existingFolder);
    }

    public void deleteFolder(Long id) {
        Folder existingFolder = folderRepository.findById(id).orElseThrow(() -> new RuntimeException("Folder not found"));
        folderRepository.delete(existingFolder);
    }
}