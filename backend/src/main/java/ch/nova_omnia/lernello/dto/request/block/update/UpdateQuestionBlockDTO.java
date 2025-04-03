package ch.nova_omnia.lernello.dto.request.block.update;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateQuestionBlockDTO(
                                     @NotNull UUID uuid,
                                     @NotBlank String name,
                                     @NotBlank String question,
                                     @NotBlank String expectedAnswer
) {
}