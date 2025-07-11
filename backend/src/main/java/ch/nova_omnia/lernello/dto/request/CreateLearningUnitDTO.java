package ch.nova_omnia.lernello.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateLearningUnitDTO(
                                    @NotBlank @Size(min = 3, max = 40) String name,
                                    @NotNull UUID learningKitId
) {
}
