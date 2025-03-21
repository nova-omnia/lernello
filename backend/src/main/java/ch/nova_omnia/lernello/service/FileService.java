package ch.nova_omnia.lernello.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.File;
import ch.nova_omnia.lernello.repository.FileRepository;

@Service
public class FileService {
    private FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<File> findAll() {
        return fileRepository.findAll();
    }

    public Optional<File> findById(UUID uuid) {
        return fileRepository.findById(uuid);
    }

    public File save(File file) {
        return fileRepository.save(file);
    }

    public void deleteById(UUID uuid) {
        fileRepository.deleteById(uuid);
    }
}
