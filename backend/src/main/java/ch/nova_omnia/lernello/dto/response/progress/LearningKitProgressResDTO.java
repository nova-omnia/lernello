package ch.nova_omnia.lernello.dto.response.progress;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record LearningKitProgressResDTO(
    @NotNull
    UUID learningKitId,
    @NotNull
    boolean isOpened,
    @NotNull
    boolean isCompleted,
    LocalDateTime firstOpenedAt,
    LocalDateTime lastOpenedAt,
    LocalDateTime completedAt,
    @NotNull
    @Size(min = 0, max = 100)
    int progressPercentage
) {
}
