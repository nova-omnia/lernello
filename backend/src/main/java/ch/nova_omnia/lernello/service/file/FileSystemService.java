package ch.nova_omnia.lernello.service.file;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
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

    @Override
    public List<File> findAll() {
        return fileRepository.findAll();
    }

    @Override
    public Optional<File> findById(UUID uuid) {
        return fileRepository.findById(uuid);
    }

    @Override
    @Transactional
    public void deleteById(UUID uuid) {
        fileRepository.deleteById(uuid);
    }

    @Override
    @Transactional
    public File save(MultipartFile file) {
        deleteExistingFileIfExists(file.getOriginalFilename());
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

    private void deleteExistingFileIfExists(String fileName) {
        Optional<File> existingFileOptional = fileRepository.findByName(fileName);
        existingFileOptional.ifPresent(existingFile -> {
            Path existingFilePath = Paths.get(storagePath, existingFile.getUuid().toString());
            try {
                Files.deleteIfExists(existingFilePath);
            } catch (IOException e) {
                throw new RuntimeException("Could not delete existing file. Error: " + e.getMessage(), e);
            }
            fileRepository.delete(existingFile);
        });
    }

    public ResponseEntity<Resource> getFileResource(UUID uuid) {
        Optional<File> fileOptional = fileRepository.findById(uuid);
        if (fileOptional.isPresent()) {
            File file = fileOptional.get();
            Path filePath = Paths.get(storagePath, uuid.toString());
            try {
                byte[] fileData = Files.readAllBytes(filePath);
                InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(fileData));
                return ResponseEntity.ok().header("attachment;filename=" + file.getName()).body(resource);
            } catch (IOException e) {
                throw new RuntimeException("Could not read file. Error: " + e.getMessage(), e);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
