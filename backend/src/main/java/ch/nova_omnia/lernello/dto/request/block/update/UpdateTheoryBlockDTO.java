package ch.nova_omnia.lernello.dto.request.block.update;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTheoryBlockDTO(
                                   @NotNull UUID uuid,
                                   @NotBlank String name,
                                   @NotBlank String content) {

}
