package ch.nova_omnia.lernello.dto.response.block;

import java.util.UUID;

import ch.nova_omnia.lernello.model.data.block.BlockType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static ch.nova_omnia.lernello.model.data.block.BlockType.QUESTION;

public record QuestionBlockResDTO(
        @NotNull BlockType type,
        @NotNull UUID uuid,
        @NotBlank String name,
        @Min(0) int position,
        @NotBlank String question,
        @NotBlank String expectedAnswer
) implements BlockResDTO {
    public QuestionBlockResDTO {
        type = QUESTION;
    }
}
