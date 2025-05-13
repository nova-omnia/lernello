package ch.nova_omnia.lernello.dto.request.progress;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record CheckMultipleChoiceAnswerDTO (
    @NotNull
    String blockId,
    @NotNull
    List<String> answers
) {
}
