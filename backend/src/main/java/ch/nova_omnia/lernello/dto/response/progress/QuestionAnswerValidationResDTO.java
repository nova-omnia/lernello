package ch.nova_omnia.lernello.dto.response.progress;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record QuestionAnswerValidationResDTO(
                                             @NotNull UUID blockId,
                                             @NotNull boolean isCorrect
) {
}
