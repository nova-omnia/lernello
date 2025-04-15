package ch.nova_omnia.lernello.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateLearningUnitDTO(
    @NotBlank
    @Size(min = 2, max = 32)
    String name,
    @NotNull
    UUID learningKitId
    ) {
}
