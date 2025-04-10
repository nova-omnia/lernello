package ch.nova_omnia.lernello.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import jakarta.persistence.EntityNotFoundException;

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
        LearningKit existingKit = learningKitRepository
                .findById(learningKit.getUuid())
                .orElseThrow(EntityNotFoundException::new);

        updateLearningKit(existingKit, learningKit);
        return existingKit;
    }

    private void updateLearningKit(LearningKit target, LearningKit source) {
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setDeadlineDate(source.getDeadlineDate());
        target.setParticipants(source.getParticipants());
        target.setContext(source.getContext());
    }


    @Transactional
    public void deleteById(UUID id) {
        learningKitRepository.deleteById(id);
    }
}
