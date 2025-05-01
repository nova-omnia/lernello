package ch.nova_omnia.lernello.service.file;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ch.nova_omnia.lernello.model.data.File;

@Service
public interface FileService {
    public List<File> findAll();

    public List<File> findAllByIds(List<UUID> uuids);

    public Optional<File> findById(UUID uuid);

    public void deleteById(UUID uuid);

    public File save(MultipartFile file);

    public String extractTextFromFiles(List<UUID> fileIds);

    public String getStoragePath(UUID id);
}
