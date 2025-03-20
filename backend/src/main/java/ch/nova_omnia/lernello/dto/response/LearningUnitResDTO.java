package ch.nova_omnia.lernello.dto.response;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LearningUnitResDTO(
                                 @NotBlank UUID uuid,
                                 @NotBlank @Size(min = 3, max = 40) String name
) {
}
