package ch.nova_omnia.lernello.dto.request.progress;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record CheckMultipleChoiceAnswerDTO (
    @NotNull
    UUID blockId,
    @NotNull
    List<String> answers
) {
}
