package ch.nova_omnia.lernello.service.file;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
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
    @Value("${server.url}")
    private String serverUrl;

    @Override
    public List<File> findAll() {
        return fileRepository.findAll();
    }

    @Override
    public List<File> findAllByIds(List<UUID> uuids) {
        return uuids == null ? null : fileRepository.findAllById(uuids);
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
        String uniqueFileName = resolveUniqueFileName(file.getOriginalFilename());
        File savedFile = fileRepository.save(new File(uniqueFileName));
        try {
            Path filePath = Paths.get(storagePath, savedFile.getUuid().toString());
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not save file. Error: " + e.getMessage(), e);
        }
        return savedFile;
    }

    @Override
    @Transactional(readOnly = true)
    public String extractTextFromFiles(List<UUID> fileIds) {
        StringBuilder context = new StringBuilder();

        for (UUID fileId : fileIds) {
            try (InputStream inputStream = loadFileAsStream(fileId); PDDocument document = PDDocument.load(inputStream)) {

                String fileContent = new PDFTextStripper().getText(document);
                if (!fileContent.isBlank()) {
                    if (context.length() > 0) {
                        context.append("\n");
                    }
                    context.append(fileContent);
                }

            } catch (IOException e) {
                throw new RuntimeException("Failed to read or parse PDF file with ID: " + fileId, e);
            }
        }

        return context.toString();
    }

    private InputStream loadFileAsStream(UUID fileId) {
        Optional<File> fileOptional = fileRepository.findById(fileId);
        if (fileOptional.isEmpty()) {
            throw new RuntimeException("File with ID " + fileId + " not found");
        }

        Path filePath = Paths.get(storagePath, fileId.toString());
        try {
            byte[] fileData = Files.readAllBytes(filePath);
            return new ByteArrayInputStream(fileData);
        } catch (IOException e) {
            throw new RuntimeException("Could not load file data for ID: " + fileId, e);
        }
    }

    private String resolveUniqueFileName(String fileName) {
        String baseName = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
        String extension = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.')) : "";
        String uniqueFileName = fileName;
        int count = 1;

        while (fileRepository.findByName(uniqueFileName).isPresent()) {
            uniqueFileName = String.format("%s (%d)%s", baseName, count++, extension);
        }

        return uniqueFileName;
    }

    public byte[] getStaticFile(UUID fileId) {
        Optional<File> fileOptional = fileRepository.findById(fileId);
        if (fileOptional.isPresent()) {
            Path filePath = Paths.get(storagePath, fileId.toString());
            try {
                return Files.readAllBytes(filePath);

            } catch (IOException e) {
                throw new RuntimeException("Could not load static file. Error: " + e.getMessage(), e);
            }
        } else {
            throw new RuntimeException("File with ID " + fileId + " not found");
        }
    }

    // public ResponseEntity<Resource> getStaticFileUrl(UUID id) {
    //     Path filePath = Paths.get(storagePath, id.toString());
    //     if (!Files.exists(filePath)) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    //     }
    //     try {
    //         FileSystemResource resource = new FileSystemResource(filePath);
    //         String contentType = Files.probeContentType(filePath);
    //         return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream")).body(resource);
    //     } catch (IOException e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    //     }
    // }
    public String getStoragePath(UUID id) {
        return Paths.get(serverUrl, id.toString()).toString();

    }
}
