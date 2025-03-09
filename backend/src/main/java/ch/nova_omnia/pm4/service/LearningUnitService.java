package ch.nova_omnia.pm4.service;

import ch.nova_omnia.pm4.model.data.Folder;
import ch.nova_omnia.pm4.model.data.LearningUnit;
import ch.nova_omnia.pm4.repository.FolderRepository;
import ch.nova_omnia.pm4.repository.LearningUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LearningUnitService {

    @Autowired
    private LearningUnitRepository learningUnitRepository;

    @Autowired
    private FolderRepository folderRepository;

    public LearningUnit createLearningUnit(String title, Long folderId) {
        LearningUnit newLearningUnit = new LearningUnit();
        newLearningUnit.setTitle(title);
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new RuntimeException("Folder not found"));
        newLearningUnit.setFolder(folder);
        return learningUnitRepository.save(newLearningUnit);
    }
}