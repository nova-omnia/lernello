package ch.nova_omnia.lernello.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateLearningUnitDTO(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 32, message = "Name does not fulfill size requirement") String name) {
}
