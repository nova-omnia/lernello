package ch.nova_omnia.lernello.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import ch.nova_omnia.lernello.dto.request.CreateFileDTO;
import ch.nova_omnia.lernello.model.data.File;
import ch.nova_omnia.lernello.repository.FileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileSystemService implements FileService {
    private final FileRepository fileRepository;

    @Value("${storage.path}")
    private String storagePath;

    public List<File> findAll() {
        return fileRepository.findAll();
    }

    public Optional<File> findById(UUID uuid) {
        return fileRepository.findById(uuid);
    }

    @Transactional
    public void deleteById(UUID uuid) {
        fileRepository.deleteById(uuid);
    }

    @Transactional
    public File save(MultipartFile file, File fileData) {
        File savedFile = fileRepository.save(fileData);

        try {
            file.transferTo(Paths.get(storagePath, savedFile.getUuid().toString()).toFile());

        } catch (Exception e) {
            throw new RuntimeException("Could not create directory for file. Error: " + e.getMessage(), e);
        }
        return savedFile;
    }

}
