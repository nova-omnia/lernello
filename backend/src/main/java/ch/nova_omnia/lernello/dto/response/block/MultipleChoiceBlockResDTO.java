package ch.nova_omnia.lernello.dto.response.block;

import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.model.data.block.BlockType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static ch.nova_omnia.lernello.model.data.block.BlockType.MULTIPLE_CHOICE;

public record MultipleChoiceBlockResDTO(
        @NotNull BlockType type,
        @NotNull UUID uuid,
        @NotBlank String name,
        @Min(0) int position,
        @NotBlank String question,
        @NotBlank List<String> possibleAnswers,
        @NotBlank List<String> correctAnswers
) implements BlockResDTO {
    public MultipleChoiceBlockResDTO {
        type = MULTIPLE_CHOICE;
    }
}
