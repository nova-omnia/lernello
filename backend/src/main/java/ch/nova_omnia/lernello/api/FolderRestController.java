package ch.nova_omnia.lernello.api;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.CreateFolderDTO;
import ch.nova_omnia.lernello.dto.response.folder.FolderResDTO;
import ch.nova_omnia.lernello.mapper.FolderMapper;
import ch.nova_omnia.lernello.service.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/folders")
@Validated
@RequiredArgsConstructor
public class FolderRestController {
    private final FolderService folderService;
    private final FolderMapper folderMapper;

    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_folders:read')")
    public List<FolderResDTO> getAllFolders() {
        return folderService.findAll().stream().map(folderMapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_folders:read')")
    public Optional<FolderResDTO> getFolderById(@PathVariable UUID id) {
        return folderService.findById(id).map(folderMapper::toDTO);
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_folders:write')")
    public @Valid FolderResDTO createFolder(@Valid @RequestBody CreateFolderDTO folder) {
        return folderMapper.toDTO(folderService.save(folderMapper.toEntity(folder)));
    }
}
