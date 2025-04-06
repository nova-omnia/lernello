package ch.nova_omnia.lernello.service.file;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import ch.nova_omnia.lernello.model.data.File;

public interface FileService {
    public List<File> findAll();

    public Optional<File> findById(UUID uuid);

    public void deleteById(UUID uuid);

    public File save(MultipartFile file);

    public ResponseEntity<Resource> getFileResource(UUID uuid);

    public String getFileContent(UUID fileId);
}
