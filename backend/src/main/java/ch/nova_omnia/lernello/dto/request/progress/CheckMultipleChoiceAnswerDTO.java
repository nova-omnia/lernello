package ch.nova_omnia.lernello.dto.request.progress;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CheckMultipleChoiceAnswerDTO (
    @NotNull
    String blockId,
    @NotNull
    List<String> answers
) {
}
