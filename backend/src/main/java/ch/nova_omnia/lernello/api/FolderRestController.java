package ch.nova_omnia.lernello.api;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.FolderDTO;
import ch.nova_omnia.lernello.mapper.FolderMapper;
import ch.nova_omnia.lernello.repository.FolderRepository;


@RestController
@RequestMapping("/api/folders")
public class FolderRestController {

    private final FolderRepository repository;
    private final FolderMapper folderMapper;

    FolderRestController(FolderRepository repository, FolderMapper folderMapper) {
        this.repository = repository;
        this.folderMapper = folderMapper;
    }


    @GetMapping()
    public List<FolderDTO> loadAll() {
        return repository.findAll().stream().map(folderMapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    public Optional<FolderDTO> getById(@PathVariable UUID id) {
        return repository.findById(id).map(folderMapper::toDTO);
    }
}
