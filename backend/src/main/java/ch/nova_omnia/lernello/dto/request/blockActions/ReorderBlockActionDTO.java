package ch.nova_omnia.lernello.dto.request.blockActions;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReorderBlockActionDTO(
        @NotBlank String blockId,
        @NotNull
        @Min(0) int newIndex
) implements BlockActionDTO {
}


