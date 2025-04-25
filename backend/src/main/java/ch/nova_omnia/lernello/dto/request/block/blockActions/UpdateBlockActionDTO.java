package ch.nova_omnia.lernello.dto.request.block.blockActions;

import ch.nova_omnia.lernello.dto.request.block.update.UpdateBlockDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import static ch.nova_omnia.lernello.dto.request.block.blockActions.BlockActionType.UPDATE;

public record UpdateBlockActionDTO(
                                    @NotNull BlockActionType type,
                                    @NotNull String blockId,
                                    @NotNull @Valid UpdateBlockDTO data
) implements BlockActionDTO {
    public UpdateBlockActionDTO {
        type = UPDATE;
    }
}
