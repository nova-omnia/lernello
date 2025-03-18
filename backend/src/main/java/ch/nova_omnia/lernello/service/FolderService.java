package ch.nova_omnia.lernello.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.Folder;
import ch.nova_omnia.lernello.repository.FolderRepository;

@Service
public class FolderService {

    private final FolderRepository folderRepository;

    @Autowired
    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public List<Folder> findAll() {
        return folderRepository.findAll();
    }

    public Optional<Folder> findById(UUID id) {
        return folderRepository.findById(id);
    }

    public Folder save(Folder folder) {
        return folderRepository.save(folder);
    }

    public void deleteById(UUID id) {
        folderRepository.deleteById(id);
    }
}