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
        if (learningKit.getUuid() == null || !learningKitRepository.existsById(learningKit.getUuid())) {
            throw new EntityNotFoundException("LearningKit not found with id: " + learningKit.getUuid());
        }
        return learningKitRepository.save(learningKit);
    }

    @Transactional
    public void deleteById(UUID id) {
        learningKitRepository.deleteById(id);
    }

    public LearningKit update(LearningKit learningKit) {
        return learningKitRepository.save(learningKit);
    }
}
