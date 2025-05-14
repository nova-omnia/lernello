package ch.nova_omnia.lernello.dto.response.progress;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TheoryBlockViewedResDTO (
    @NotNull
    UUID blockId,
    @NotNull
    Boolean isViewed
) {
}
