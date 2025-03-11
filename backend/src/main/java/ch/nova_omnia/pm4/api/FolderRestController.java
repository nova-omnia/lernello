package ch.nova_omnia.pm4.api;

import ch.nova_omnia.pm4.dto.FolderDto;
import ch.nova_omnia.pm4.mapper.FolderMapper;
import ch.nova_omnia.pm4.model.data.Folder;
import ch.nova_omnia.pm4.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/folders")
public class FolderRestController extends AbstractCrudRestController<Folder, FolderDto, Long> {

    @Autowired
    private FolderService folderService;

    @Autowired
    private FolderMapper folderMapper;

    @Override
    protected FolderService getService() {
        return folderService;
    }

    @Override
    protected FolderMapper getMapper() {
        return folderMapper;
    }
}