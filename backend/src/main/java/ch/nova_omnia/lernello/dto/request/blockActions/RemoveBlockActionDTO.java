package ch.nova_omnia.lernello.dto.request.blockActions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static ch.nova_omnia.lernello.dto.request.blockActions.BlockActionType.REMOVE;

public record RemoveBlockActionDTO(
        @NotNull BlockActionType type,
        @NotBlank String blockId
) implements BlockActionDTO {
    public RemoveBlockActionDTO {
        type = REMOVE;
    }
}
