package ch.nova_omnia.pm4.service;

import ch.nova_omnia.pm4.model.data.Folder;
import ch.nova_omnia.pm4.model.data.LearningUnit;
import ch.nova_omnia.pm4.repository.FolderRepository;
import ch.nova_omnia.pm4.repository.LearningUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * LearningUnitService is a service class that provides business logic for managing {@link LearningUnit} entities.
 */
@Service
public class LearningUnitService {

    @Autowired
    private LearningUnitRepository learningUnitRepository;

    @Autowired
    private FolderRepository folderRepository;

    /**
     * Creates a new learning unit and associates it with a specified folder.
     * 
     * @param name the name of the learning unit
     * @param folderId the ID of the folder to associate with the learning unit
     * @return the created learning unit
     * @throws RuntimeException if the folder is not found
     */
    public LearningUnit createLearningUnit(String name, Long folderId) {
        LearningUnit newLearningUnit = new LearningUnit();
        newLearningUnit.setName(name);
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new RuntimeException("Folder not found"));
        newLearningUnit.setFolder(folder);
        return learningUnitRepository.save(newLearningUnit);
    }
}