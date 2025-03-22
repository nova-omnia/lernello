package ch.nova_omnia.lernello.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ch.nova_omnia.lernello.model.data.MediaFile;
import ch.nova_omnia.lernello.repository.MediaFileRepository;

@Service
public class MediaFileService {
    private final MediaFileRepository fileRepository;
    private final Path fileStorageLocation;

    public MediaFileService(MediaFileRepository fileRepository) {
        this.fileRepository = fileRepository;
        this.fileStorageLocation = Paths.get("file_storage").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public List<MediaFile> findAll() {
        return fileRepository.findAll();
    }

    public Optional<MediaFile> findById(UUID uuid) {
        return fileRepository.findById(uuid);
    }

    public void deleteById(UUID uuid) {
        fileRepository.deleteById(uuid);
    }

    public MediaFile save(MediaFile file) {
        return fileRepository.save(file);
    }

    public void storeFile(MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            file.transferTo(targetLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not store file ", ex);
        }
    }

    public Path loadFile(String fileName) {
        return this.fileStorageLocation.resolve(fileName).normalize();
    }


}
