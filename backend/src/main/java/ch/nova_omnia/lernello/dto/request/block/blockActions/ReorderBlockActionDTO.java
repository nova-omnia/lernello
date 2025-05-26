package ch.nova_omnia.lernello.dto.request.block.blockActions;

import static ch.nova_omnia.lernello.dto.request.block.blockActions.BlockActionType.REORDER_BLOCK;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReorderBlockActionDTO(
                                    @NotNull BlockActionType type,
                                    @NotBlank String blockId,
                                    @NotNull @Min(0) int newIndex,
                                    @NotNull String language
) implements BlockActionDTO {
    public ReorderBlockActionDTO {
        type = REORDER_BLOCK;
    }
}

