package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.model.data.Folder;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.repository.FolderRepository;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LearningKitService {
    private final LearningKitRepository learningKitRepository;

    public LearningKitService(LearningKitRepository learningKitRepository) {
        this.learningKitRepository = learningKitRepository;
    }

    public List<LearningKit> findAll() {
        return learningKitRepository.findAll();
    }

    public Optional<LearningKit> findById(UUID id) {
        return learningKitRepository.findById(id);
    }

    @Transactional
    public LearningKit save(LearningKit learningKit) {
        return learningKitRepository.save(learningKit);
    }

    @Transactional
    public LearningKit edit(LearningKit learningKit) {
        LearningKit learningKitToEdit = learningKitRepository.findById(learningKit.getUuid()).orElseThrow();
        learningKitToEdit.setName(learningKit.getName());
        learningKitToEdit.setDescription(learningKit.getDescription());
        learningKitToEdit.setLanguage(learningKit.getLanguage());
        learningKitToEdit.setDeadlineDate(learningKit.getDeadlineDate());
        learningKitToEdit.setParticipants(learningKit.getParticipants());
        learningKitToEdit.setFolder(learningKit.getFolder());
        // learningKitToEdit.setFiles(learningKit.getFiles()); // ToDo
        // learningKitToEdit.setLearningUnits(learningKit.getLearningUnits()); // ToDo

        return learningKitToEdit;
    }

    @Transactional
    public void deleteById(UUID id) {
        learningKitRepository.deleteById(id);
    }
}
