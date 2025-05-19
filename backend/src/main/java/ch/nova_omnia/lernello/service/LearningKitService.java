package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.progress.LearningKitProgress;
import ch.nova_omnia.lernello.model.data.user.Role;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class LearningKitService {
    private final LearningKitRepository learningKitRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ProgressService progressService;

    public Page<LearningKit> getList(Pageable pageable, UUID userID) {
        if (isTrainee(userID)) {
            return learningKitRepository.findAllByTrainees_UuidAndPublishedTrue(userID, pageable);
        }
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
    public void removeTrainee(UUID learningKitId, UUID userId) {
        LearningKit kit = learningKitRepository.findById(learningKitId).orElseThrow(() -> new EntityNotFoundException("LearningKit not found"));

        boolean removed = kit.getTrainees().removeIf(user -> user.getUuid().equals(userId));

        if (removed) {
            learningKitRepository.save(kit);
        } else {
            throw new IllegalArgumentException("Trainee not found in this LearningKit");
        }
    }

    @Transactional
    public void publishLearningKit(UUID learningKitId) {
        LearningKit kit = learningKitRepository.findById(learningKitId).orElseThrow(() -> new EntityNotFoundException("LearningKit not found"));
        ArrayList<User> trainees = new ArrayList<>(kit.getTrainees());
        for (User trainee : trainees) {
            if (trainee.getRole() == Role.TRAINEE) {
                emailService.sendLearningKitInvitation(trainee, kit);
            }
        }
        kit.setPublished(true);
    }

    @Transactional
    public void saveTraineeInKit(UUID learningKitId, User trainee) {
        LearningKit kit = learningKitRepository.findById(learningKitId).orElseThrow(() -> new EntityNotFoundException("LearningKit not found"));
        kit.getTrainees().add(trainee);
        learningKitRepository.save(kit);
    }

    private boolean isTrainee(UUID id) {
        User user = userRepository.findByUuid(id);
        return user.getRole() == Role.TRAINEE;
    }

    public void reorderLearningUnits(LearningKit learningKit, String[] learningUnitIds) {
        //todo
    }

    public void updateLearningKitStatistics(LearningKit learningKit) {
        updateLearningKitAverageProgress(learningKit);
        updateLearningKitCompletionRate(learningKit);
    }

    private void updateLearningKitAverageProgress(LearningKit learningKit) {
        List<LearningKitProgress> kitProgresses = progressService.getLearningKitProgressForAllTrainees(learningKit.getUuid());
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
        List<LearningKitProgress> kitProgresses = progressService.getLearningKitProgressForAllTrainees(learningKit.getUuid());
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
