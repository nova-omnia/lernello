package ch.nova_omnia.lernello.dto.request.blockActions;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static ch.nova_omnia.lernello.dto.request.blockActions.BlockActionType.REORDER;

public record ReorderBlockActionDTO(
        @NotNull BlockActionType type,
        @NotBlank String blockId,
        @NotNull
        @Min(0) int newIndex
) implements BlockActionDTO {
        public ReorderBlockActionDTO {
                type = REORDER;
        }
}


