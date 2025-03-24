package ch.nova_omnia.lernello.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ch.nova_omnia.lernello.model.data.File;
import ch.nova_omnia.lernello.repository.FileRepository;
import lombok.RequiredArgsConstructor;

@Service
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
    public File save(MultipartFile file) {
        File savedFile = fileRepository.save(new File(file.getOriginalFilename()));
        try {
            Path filePath = Paths.get(storagePath, savedFile.getUuid().toString());
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create directory for file. Error: " + e.getMessage(), e);
        }
        return savedFile;
    }

    public byte[] readFileData(UUID uuid) {
        Optional<File> fileOptional = fileRepository.findById(uuid);
        if (fileOptional.isPresent()) {
            Path filePath = Paths.get(storagePath, uuid.toString());
            try {
                return Files.readAllBytes(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Could not read file. Error: " + e.getMessage(), e);
            }
        } else {
            throw new RuntimeException("File not found with UUID: " + uuid);
        }
    }
}
