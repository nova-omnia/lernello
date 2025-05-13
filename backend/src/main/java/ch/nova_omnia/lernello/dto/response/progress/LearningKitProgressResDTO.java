package ch.nova_omnia.lernello.dto.response.progress;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.UUID;

public record LearningKitProgressResDTO(
    @NotNull
    String learningKitId,
    @NotNull
    UUID userId,
    @NotNull
    boolean isOpened,
    @NotNull
    boolean isCompleted,
    ZonedDateTime firstOpenedAt,
    ZonedDateTime lastOpenedAt,
    ZonedDateTime completedAt,
    @NotNull
    @Min(0)
    @Max(100)
    int progressPercentage
) {
}
