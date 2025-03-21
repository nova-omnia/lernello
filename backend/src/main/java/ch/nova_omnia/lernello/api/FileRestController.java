package ch.nova_omnia.lernello.api;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.UploadFileDTO;
import ch.nova_omnia.lernello.dto.response.FileResDTO;
import ch.nova_omnia.lernello.mapper.FileMapper;
import ch.nova_omnia.lernello.service.FileService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/files")
@Validated
public class FileRestController {
    FileService fileService;
    FileMapper fileMapper;

    public FileRestController(FileService fileService, FileMapper fileMapper) {
        this.fileService = fileService;
        this.fileMapper = fileMapper;
    }

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

    @PostMapping()
    @PreAuthorize("hasAuthority('SCOPE_files:write')")
    public @Valid FileResDTO create(@Valid @RequestBody UploadFileDTO file) {
        var entity = fileMapper.toEntity(file);
        var savedEntity = fileService.save(entity);
        return fileMapper.toDTO(savedEntity);
    }
}
