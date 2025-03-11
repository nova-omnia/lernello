package ch.nova_omnia.pm4.service;

import ch.nova_omnia.pm4.model.data.Folder;
import ch.nova_omnia.pm4.repository.FolderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FolderService extends AbstractCrudService<Folder, Long> {

    @Autowired
    private FolderRepository folderRepository;

    @Override
    protected JpaRepository<Folder, Long> getRepository() {
        return folderRepository;
    }
}