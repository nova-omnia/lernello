package ch.nova_omnia.lernello.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.nova_omnia.lernello.dto.request.UpdateLearningKitDTO;
import ch.nova_omnia.lernello.mapper.LearningKitMapper;
import ch.nova_omnia.lernello.model.data.File;
import ch.nova_omnia.lernello.model.data.User;
import ch.nova_omnia.lernello.repository.FileRepository;
import ch.nova_omnia.lernello.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import jakarta.persistence.EntityNotFoundException;

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

    public LearningKit update(LearningKit learningKit, List<UUID> participantIds, List<UUID> fileIds) {
        List<User> participants = userRepository.findAllById(participantIds);
        List<File> files = fileRepository.findAllById(fileIds);
        learningKit.setParticipants(participants);
        learningKit.setFiles(files);
        return learningKitRepository.save(learningKit);
    }
}
