package ch.nova_omnia.lernello.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.MediaFile;
import ch.nova_omnia.lernello.repository.MediaFileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MediaFileService {
    private final MediaFileRepository fileRepository;

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
}
