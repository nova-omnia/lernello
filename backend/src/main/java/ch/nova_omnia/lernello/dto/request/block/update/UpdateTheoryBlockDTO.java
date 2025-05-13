package ch.nova_omnia.lernello.dto.request.block.update;

import java.util.UUID;

import ch.nova_omnia.lernello.model.data.block.BlockType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static ch.nova_omnia.lernello.model.data.block.BlockType.THEORY;

public record UpdateTheoryBlockDTO(
                                   @NotNull BlockType type,
                                   @NotNull UUID uuid,
                                   @NotBlank String name,
                                   String content
) implements UpdateBlockDTO {
    public UpdateTheoryBlockDTO {
        type = THEORY;
    }
}
