package ch.nova_omnia.lernello.dto.request.block.update;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateMultipleChoiceBlockDTO(
                                           @NotNull UUID uuid,
                                           @NotBlank String name,
                                           @NotBlank String question,
                                           @NotNull List<String> possibleAnswers,
                                           @NotNull List<String> correctAnswers) {

}
