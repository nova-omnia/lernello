package ch.nova_omnia.lernello.dto.response.progress;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MultipleChoiceAnswerValidationResDTO(
    @NotNull
    UUID blockId,
    @NotNull
    boolean isCorrect
) {
}
