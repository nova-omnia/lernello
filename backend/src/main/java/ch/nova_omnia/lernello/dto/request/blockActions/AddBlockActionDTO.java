package ch.nova_omnia.lernello.dto.request.blockActions;

import ch.nova_omnia.lernello.dto.request.block.create.CreateBlockDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddBlockActionDTO(
        @NotNull String type,
        @Min(0) Integer index,
        @NotNull String blockId,
        @NotNull @Valid CreateBlockDTO data
) implements BlockActionDTO {
}