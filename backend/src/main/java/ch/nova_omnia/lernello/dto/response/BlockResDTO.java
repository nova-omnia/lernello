package ch.nova_omnia.lernello.dto.response;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record BlockResDTO(
        @NotBlank
        UUID uuid,
        @NotBlank
        @Pattern(regexp = "^(quiz|theory|multimedia)$", message = "Type must be either 'quiz', 'theory' or 'multimedia'")
        String type,
        @NotBlank
        LearningUnit learningUnit,
        @NotBlank
        @Size(min = 3, max = 40, message = "the name does not fulfill the size requirements")
        String name
) {
}
