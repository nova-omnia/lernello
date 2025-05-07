package ch.nova_omnia.lernello.api;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import ch.nova_omnia.lernello.service.file.FileSystemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/files")
@Validated
@RequiredArgsConstructor
public class FileRestController {
    private final FileSystemService fileService;
    private final FileMapper fileMapper;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_files:write')")
    public @Valid FileResDTO uploadFile(@RequestParam("file") MultipartFile file) {
        return fileMapper.toDTO(fileService.save(file));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_files:read')")
    public Optional<FileResDTO> getFileById(@PathVariable UUID id) {
        return fileService.findById(id).map(fileMapper::toDTO);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_files:write')")
    public void deleteFile(@PathVariable UUID id) {
        fileService.deleteById(id);
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_files:read')")
    public List<@Valid FileResDTO> getAllFiles() {
        return fileService.findAll().stream().map(fileMapper::toDTO).toList();
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_files:read')")
    public Page<@Valid FileResDTO> getList(@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<File> files = fileService.getList(pageable);
        return files.map(fileMapper::toDTO);
    }
}
