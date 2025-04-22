package ch.nova_omnia.lernello.dto.request.block.create;

import static ch.nova_omnia.lernello.model.data.block.BlockType.QUESTION;

import ch.nova_omnia.lernello.model.data.block.BlockType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateQuestionBlockDTO(
        @NotNull BlockType type,
        @Size(min = 3, max = 40)
        @NotBlank String name,
        @Min(0) int position,
        @NotBlank String question,
        @NotBlank String expectedAnswer
) implements CreateBlockDTO {
    public CreateQuestionBlockDTO {
        type = QUESTION;
    }
}