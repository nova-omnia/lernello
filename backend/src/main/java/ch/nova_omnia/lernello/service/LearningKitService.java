package ch.nova_omnia.lernello.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.progress.LearningKitProgress;
import ch.nova_omnia.lernello.model.data.user.Role;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class LearningKitService {
    private final LearningKitRepository learningKitRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ProgressService progressService;

    public Page<LearningKit> getList(Pageable pageable, UUID userID) {
        if (isParticipant(userID)) {
            return learningKitRepository.findAllByParticipants_UuidAndPublishedTrue(userID, pageable);
        }
        return learningKitRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Optional<LearningKit> findById(UUID id) {
        updateLearningKitAverageProgress(learningKitRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("LearningKit not found")));
        updateLearningKitCompletionRate(learningKitRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("LearningKit not found")));
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
        for (User participant : participants) {
            if (participant.getRole() == Role.TRAINEE) {
                emailService.sendLearningKitInvitation(participant, kit);
            }
        }
        kit.setPublished(true);
    }

    @Transactional
    public void saveTraineeInKit(UUID learningKitId, User trainee) {
        LearningKit kit = learningKitRepository.findById(learningKitId).orElseThrow(() -> new EntityNotFoundException("LearningKit not found"));
        kit.getParticipants().add(trainee);
        learningKitRepository.save(kit);
    }

    private boolean isParticipant(UUID id) {
        User user = userRepository.findByUuid(id);
        return user.getRole() == Role.TRAINEE;
    }

    public void reorderLearningUnits(LearningKit learningKit, String[] learningUnitIds) {
    //todo
    }

    private void updateLearningKitAverageProgress(LearningKit learningKit) {
        List<LearningKitProgress> kitProgresses= progressService.getLearningKitProgressForAllParticipants(learningKit.getUuid());
        if (kitProgresses != null) {
            int totalProgress = 0;
            int count = 0;
            for (LearningKitProgress progress : kitProgresses) {
                totalProgress += progress.getProgressPercentage();
                count++;
            }
            if (count > 0) {
                int averageProgress = totalProgress / count;
                learningKit.setAverageProgress(averageProgress);
            } else {
                learningKit.setAverageProgress(0);
            }
        } else {
            learningKit.setAverageProgress(0);
        }
    }

    private void updateLearningKitCompletionRate(LearningKit learningKit) {
        List<LearningKitProgress> kitProgresses = progressService.getLearningKitProgressForAllParticipants(learningKit.getUuid());
        if (kitProgresses != null) {
            int completedCount = 0;
            int totalCount = 0;
            for (LearningKitProgress progress : kitProgresses) {
                if (progress.isCompleted()) {
                    completedCount++;
                }
                totalCount++;
            }
            if (totalCount > 0) {
                int completionRate = (completedCount * 100) / totalCount;
                learningKit.setCompletionRate(completionRate);
            } else {
                learningKit.setCompletionRate(0);
            }
        } else {
            learningKit.setCompletionRate(0);
        }
    }
}
