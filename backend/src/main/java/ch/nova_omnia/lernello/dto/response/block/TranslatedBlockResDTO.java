package ch.nova_omnia.lernello.dto.response.block;

import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.model.data.block.BlockLanguage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TranslatedBlockResDTO(
                                   @NotNull UUID id,
                                   @NotBlank BlockLanguage language,
                                   String content,
                                   String question,
                                   String expectedAnswer,
                                   List<String> possibleAnswers,
                                   List<String> correctAnswers
) {
}
