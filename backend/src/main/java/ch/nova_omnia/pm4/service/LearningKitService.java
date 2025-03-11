package ch.nova_omnia.pm4.service;

import ch.nova_omnia.pm4.model.data.Folder;
import ch.nova_omnia.pm4.model.data.LearningKit;
import ch.nova_omnia.pm4.repository.FolderRepository;
import ch.nova_omnia.pm4.repository.LearningKitRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * LearningKitService is a service class that provides business logic for managing {@link LearningKit} entities.
 */
@Service
public class LearningKitService {

    @Autowired
    private LearningKitRepository learningKitRepository;

    @Autowired
    private FolderRepository folderRepository;


    public LearningKit createLearningKit(String name, Long folderId) {
        LearningKit newLearningKit = new LearningKit();
        newLearningKit.setName(name);
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new RuntimeException("Folder not found"));
        newLearningKit.setParentFolder(folder);
        return learningKitRepository.save(newLearningKit);
    }

    public List<LearningKit> getAllLearningKits() {
        return (List<LearningKit>) learningKitRepository.findAll();
    }

    public LearningKit getLearningKitById(Long id) {
        return learningKitRepository.findById(id).orElseThrow(() -> new RuntimeException("Learning kit not found"));
    }

    public LearningKit updateLearningKit(Long id, LearningKit learningKit) {
        LearningKit existingLearningKit = learningKitRepository.findById(id).orElseThrow(() -> new RuntimeException("Learning kit not found"));
        existingLearningKit.setName(learningKit.getName());
        return learningKitRepository.save(existingLearningKit);
    }

    public void deleteLearningKit(Long id) {
        learningKitRepository.deleteById(id);
    }

    public LearningKit setParentFolder(Long id, Long parentId) {
        LearningKit learningKit = learningKitRepository.findById(id).orElseThrow(() -> new RuntimeException("Learning kit not found"));
        Folder parentFolder = folderRepository.findById(parentId).orElseThrow(() -> new RuntimeException("Folder not found"));
        learningKit.setParentFolder(parentFolder);
        return learningKitRepository.save(learningKit);
    }
}