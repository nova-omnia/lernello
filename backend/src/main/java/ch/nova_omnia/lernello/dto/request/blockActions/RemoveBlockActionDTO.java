package ch.nova_omnia.lernello.dto.request.blockActions;

import jakarta.validation.constraints.NotBlank;

public record RemoveBlockActionDTO(
        @NotBlank String blockId
) implements BlockActionDTO {

}
