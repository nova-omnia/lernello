package ch.nova_omnia.lernello.dto.response.progress;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record QuestionAnswerValidationResDTO (
    @NotNull
    String blockId,
    @NotNull
    boolean isCorrect
) {
}
