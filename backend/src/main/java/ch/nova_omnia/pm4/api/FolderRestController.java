package ch.nova_omnia.pm4.api;

import ch.nova_omnia.pm4.model.data.Folder;
import ch.nova_omnia.pm4.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
public class FolderRestController {

    @Autowired
    private FolderService folderService;

    @GetMapping("/{id}")
    public Folder getFolder(@PathVariable Long id) {
        return folderService.getFolderById(id);
    }

    @GetMapping
    public List<Folder> getAllFolders() {
        return folderService.getAllFolders();
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

    @PutMapping("/{id}/parent/{parentId}")
    public Folder setParentFolder(@PathVariable Long id, @PathVariable Long parentId) {
        return folderService.setParentFolder(id, parentId);
    }
}