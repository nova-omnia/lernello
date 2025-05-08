package ch.nova_omnia.lernello.dto.response.block;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocalizedBlockResDTO(
                                   @NotNull UUID id,
                                   @NotBlank String languageCode,
                                   String content,
                                   String question,
                                   String expectedAnswer,
                                   List<String> possibleAnswers,
                                   List<String> correctAnswers
) {
}
