package ch.nova_omnia.lernello.dto.request.progress;

import jakarta.validation.constraints.NotNull;

public record TheoryBlockViewedDTO(
    @NotNull
    String blockId
) {
}
