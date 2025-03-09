package ch.nova_omnia.pm4.api;

import ch.nova_omnia.pm4.model.data.Folder;
import ch.nova_omnia.pm4.model.data.FolderDTO;
import ch.nova_omnia.pm4.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/folders")
public class FolderRestController {

    @Autowired
    private FolderService folderService;

    @GetMapping("/{id}")
    public FolderDTO getFolder(@PathVariable Long id, @RequestParam(defaultValue = "1") int depth) {
        Folder folder = folderService.getFolderById(id);
        return folder.toDTO(depth);
    }

    @GetMapping
    public List<FolderDTO> getAllFolders(@RequestParam(defaultValue = "1") int depth) {
        List<Folder> folders = folderService.getAllFolders();
        return folders.stream()
                .map(folder -> folder.toDTO(depth))
                .collect(Collectors.toList());
    }

    @PostMapping
    public Folder createFolder(@RequestBody Folder folder) {
        return folderService.createFolder(folder);
    }

    @PutMapping("/{id}")
    public Folder updateFolder(@PathVariable Long id, @RequestBody Folder folder) {
        return folderService.updateFolder(id, folder);
    }

    @DeleteMapping("/{id}")
    public void deleteFolder(@PathVariable Long id) {
        folderService.deleteFolder(id);
    }
}