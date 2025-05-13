package ch.nova_omnia.lernello.dto.request.progress;

import jakarta.validation.constraints.NotNull;

public record LearningUnitOpenedDTO(
    @NotNull
    String learningUnitId
) {
}
