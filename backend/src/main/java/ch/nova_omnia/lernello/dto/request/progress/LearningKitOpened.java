package ch.nova_omnia.lernello.dto.request.progress;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LearningKitOpened(
    @NotNull
    UUID learningKitId
) {
}
