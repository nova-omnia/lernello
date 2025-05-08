package ch.nova_omnia.lernello.dto.request.block.blockActions;

import static ch.nova_omnia.lernello.dto.request.block.blockActions.BlockActionType.REMOVE_BLOCK;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RemoveBlockActionDTO(
                                   @NotNull BlockActionType type,
                                   @NotBlank String blockId
) implements BlockActionDTO {
    public RemoveBlockActionDTO {
        type = REMOVE_BLOCK;
    }
}
