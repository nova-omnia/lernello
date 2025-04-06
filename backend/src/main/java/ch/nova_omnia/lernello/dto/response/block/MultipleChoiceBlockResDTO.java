package ch.nova_omnia.lernello.dto.response.block;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MultipleChoiceBlockResDTO(
        @NotNull UUID uuid,
        @NotBlank String name,
        @Min(0) int position,
        @NotBlank String question,
        @NotBlank List<String> possibleAnswers,
        @NotBlank List<String> correctAnswers
) implements BlockResDTO {
}
