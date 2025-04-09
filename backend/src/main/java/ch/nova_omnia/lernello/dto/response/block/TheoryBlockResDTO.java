package ch.nova_omnia.lernello.dto.response.block;

import java.util.UUID;

import ch.nova_omnia.lernello.model.data.block.BlockType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static ch.nova_omnia.lernello.model.data.block.BlockType.THEORY;


public record TheoryBlockResDTO(
        @NotNull BlockType type,
        @NotNull UUID uuid,
        @Size (min = 3, max = 40)
        @NotBlank String name,
        @Min(0) int position,
        @NotBlank String content
) implements BlockResDTO {
    public TheoryBlockResDTO {
        type = THEORY;
    }
}