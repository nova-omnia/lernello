package ch.nova_omnia.lernello.dto.response.progress;

import ch.nova_omnia.lernello.model.data.block.BlockType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import static ch.nova_omnia.lernello.model.data.block.BlockType.THEORY;

public record TheoryBlockProgressResDTO(
    @NotNull
    UUID blockId,
    @NotNull
    Boolean isViewed
) implements BlockProgressResDTO {
    @Override
    public BlockType blockType() {
        return THEORY;
    }
}
