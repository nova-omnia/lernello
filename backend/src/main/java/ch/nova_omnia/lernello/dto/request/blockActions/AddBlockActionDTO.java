package ch.nova_omnia.lernello.dto.request.blockActions;

import ch.nova_omnia.lernello.dto.request.block.create.CreateBlockDTO;
import ch.nova_omnia.lernello.model.data.blocks.BlockType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddBlockActionDTO (
        @NotBlank BlockType type,  // "quiz" or "theory"
        @Min(0) Integer index,
        @NotBlank String name,
        @NotNull String blockId,
        @NotNull CreateBlockDTO blockPayload
) implements BlockActionDTO {}
