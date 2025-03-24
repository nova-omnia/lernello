package ch.nova_omnia.lernello.api;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.nova_omnia.lernello.dto.response.MediaFileResDTO;
import ch.nova_omnia.lernello.mapper.MediaFileMapper;
import ch.nova_omnia.lernello.model.data.MediaFile;
import ch.nova_omnia.lernello.service.MediaFileService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/media_files")
@Validated
public class MediaFileRestController {
    private final MediaFileService fileService;
    private final MediaFileMapper fileMapper;


    public MediaFileRestController(MediaFileService fileService, MediaFileMapper fileMapper) {
        this.fileService = fileService;
        this.fileMapper = fileMapper;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('SCOPE_files:read')")
    public List<@Valid MediaFileResDTO> loadAll() {
        return fileService.findAll().stream().map(fileMapper::toDTO).toList();
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_files:read')")
    public Optional<MediaFileResDTO> getById(@PathVariable UUID id) {
        return fileService.findById(id).map(fileMapper::toDTO);
    }

    @GetMapping("/static/{id}")
    @PreAuthorize("hasAuthority('SCOPE_files:read')")
    public Optional<MediaFileResDTO> getFile(@PathVariable UUID id) {
        return fileService.findById(id).map(fileMapper::toDTO);
    }

    @DeleteMapping()
    @PreAuthorize("hasAuthority('SCOPE_files:write')")
    public void deleteById(@PathVariable UUID id) {
        fileService.deleteById(id);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('SCOPE_files:write')")
    public ResponseEntity<MediaFileResDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        MediaFile entity = new MediaFile();
        entity.setName(file.getOriginalFilename());
        entity.setUuid(UUID.randomUUID());
        MediaFile savedEntity = fileService.save(entity);
        return ResponseEntity.ok(fileMapper.toDTO(savedEntity));
    }
}
