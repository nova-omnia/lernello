package ch.nova_omnia.lernello.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.nova_omnia.lernello.model.data.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LearningKitService {
    private final LearningKitRepository learningKitRepository;
    private final EmailService emailService;

    public Page<LearningKit> getList(Pageable pageable) {
        return learningKitRepository.findAllByOrderByCreatedAtDesc(pageable);
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

    @Transactional
    public void publishLearningKit(UUID learningKitId) {
        LearningKit kit = learningKitRepository.findById(learningKitId).orElseThrow(() -> new EntityNotFoundException("LearningKit not found"));
        ArrayList<User> participants = new ArrayList<>(kit.getParticipants());
        for(User participant : participants) {
            if (participant.getRole() == User.Role.TRAINEE) {
                emailService.sendLearningKitInvitation(participant, kit);
            }
        }
    }
}
