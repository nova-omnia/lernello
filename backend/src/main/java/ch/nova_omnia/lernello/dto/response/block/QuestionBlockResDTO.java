package ch.nova_omnia.lernello.dto.response.block;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionBlockResDTO(
        @NotNull UUID uuid,
        @NotBlank String name,
        @Min(0) int position,
        @NotBlank String question,
        @NotBlank String expectedAnswer
) implements BlockResDTO {
}
