package ch.nova_omnia.lernello.dto.response.block;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MulipleChoiceBlockResDTO(
                                       @NotNull UUID uuid,
                                       @NotBlank String name,
                                       @NotNull @Min(0) int position,       
                                       @NotBlank String question,
                                       @NotNull List<String> possibleAnswers,
                                       @NotNull List<String> correctAnswers) {
}
