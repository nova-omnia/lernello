package ch.nova_omnia.lernello.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.model.data.Folder;
import ch.nova_omnia.lernello.repository.FolderRepository;

@Service
public class FolderService {

    private final FolderRepository folderRepository;

    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public List<Folder> findAll() {
        return folderRepository.findAll();
    }

    public Optional<Folder> findById(UUID id) {
        return folderRepository.findById(id);
    }

    @Transactional
    public Folder save(Folder folder) {
        return folderRepository.save(folder);
    }

    @Transactional
    public void deleteById(UUID id) {
        folderRepository.deleteById(id);
    }
}