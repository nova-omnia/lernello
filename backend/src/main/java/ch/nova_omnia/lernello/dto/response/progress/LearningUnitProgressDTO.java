package ch.nova_omnia.lernello.dto.response.progress;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record LearningUnitProgressDTO(
    @NotNull
    String learningUnitId,
    @NotNull
    boolean isOpened,
    @NotNull
    boolean isCompleted,
    LocalDateTime firstOpenedAt,
    LocalDateTime lastOpenedAt,
    LocalDateTime completedAt,
    @NotNull
    @Min(0)
    @Max(100)
    int progressPercentage
) {
}
