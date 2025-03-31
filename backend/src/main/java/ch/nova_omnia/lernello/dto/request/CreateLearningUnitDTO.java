package ch.nova_omnia.lernello.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateLearningUnitDTO(@NotBlank @Size(min = 2, max = 32) String name) {
}
