package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.model.data.Folder;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.repository.FolderRepository;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import jakarta.persistence.EntityNotFoundException;
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
        return learningKitRepository.save(learningKit);
    }

    private void updateLearningKit(LearningKit target, LearningKit source) {
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setLanguage(source.getLanguage());
        target.setDeadlineDate(source.getDeadlineDate());
        target.getParticipants().clear();
        if (source.getParticipants() != null) {
            target.getParticipants().addAll(source.getParticipants());
        }
        target.setFolder(source.getFolder());
        target.setContext(source.getContext());
    }


    @Transactional
    public void deleteById(UUID id) {
        learningKitRepository.deleteById(id);
    }
}
