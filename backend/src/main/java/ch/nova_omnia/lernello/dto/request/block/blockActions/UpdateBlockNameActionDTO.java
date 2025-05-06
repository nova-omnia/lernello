package ch.nova_omnia.lernello.dto.request.block.blockActions;

import static ch.nova_omnia.lernello.dto.request.block.blockActions.BlockActionType.UPDATE_BLOCK_NAME;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBlockNameActionDTO(
        @NotNull BlockActionType type,
        @NotNull String blockId,
        @NotBlank String newName
) implements BlockActionDTO {
    public UpdateBlockNameActionDTO {
        type = UPDATE_BLOCK_NAME;
    }
}
