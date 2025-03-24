package ch.nova_omnia.lernello.api;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

import ch.nova_omnia.lernello.dto.response.FileResDTO;
import ch.nova_omnia.lernello.mapper.FileMapper;
import ch.nova_omnia.lernello.model.data.File;
import ch.nova_omnia.lernello.service.FileSystemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/files")
@Validated
@RequiredArgsConstructor
public class FileRestController {
    private final FileSystemService fileService;
    private final FileMapper fileMapper;

    @GetMapping()
    @PreAuthorize("hasAuthority('SCOPE_files:read')")
    public List<@Valid FileResDTO> loadAll() {
        return fileService.findAll().stream().map(fileMapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_files:read')")
    public Optional<FileResDTO> getById(@PathVariable UUID id) {
        return fileService.findById(id).map(fileMapper::toDTO);
    }

    @GetMapping("/static/{id}")
    @PreAuthorize("hasAuthority('SCOPE_files:read')")
    public ResponseEntity<Resource> getFile(@PathVariable UUID id) {
        Optional<File> fileOptional = fileService.findById(id);
        if (fileOptional.isPresent()) {
            File file = fileOptional.get();
            byte[] fileData = fileService.readFileData(id);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(fileData));
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName()).contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(fileData.length).body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_files:write')")
    public void deleteById(@PathVariable UUID id) {
        fileService.deleteById(id);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('SCOPE_files:write')")
    public FileResDTO uploadFile(@RequestParam("file") MultipartFile file) {
        return fileMapper.toDTO(fileService.save(file));
    }
}