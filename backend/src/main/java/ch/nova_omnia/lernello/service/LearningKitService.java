package ch.nova_omnia.lernello.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.repository.FileRepository;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearningKitService {
    private final LearningKitRepository learningKitRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

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
    public void deleteById(UUID id) {
        learningKitRepository.deleteById(id);
    }

    @Transactional
    public void removeParticipant(UUID learningKitId, UUID userId) {
        LearningKit kit = learningKitRepository.findById(learningKitId).orElseThrow(() -> new EntityNotFoundException("LearningKit not found"));

        boolean removed = kit.getParticipants().removeIf(user -> user.getUuid().equals(userId));

        if (removed) {
            learningKitRepository.save(kit);
        } else {
            throw new IllegalArgumentException("Participant not found in this LearningKit");
        }
    }
}
