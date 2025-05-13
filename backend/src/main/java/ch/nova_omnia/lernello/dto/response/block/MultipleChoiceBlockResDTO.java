package ch.nova_omnia.lernello.dto.response.block;

import static ch.nova_omnia.lernello.model.data.block.BlockType.MULTIPLE_CHOICE;

import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.model.data.block.BlockType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MultipleChoiceBlockResDTO(
                                        @NotNull BlockType type,
                                        @NotNull UUID uuid,
                                        @Size(min = 3, max = 40) @NotBlank String name,
                                        @Min(0) int position,
                                        @NotBlank String question,
                                        @NotNull List<String> possibleAnswers,
                                        @NotNull List<String> correctAnswers,
                                        @NotNull List<TranslatedBlockResDTO> translatedContents
) implements BlockResDTO {
    public MultipleChoiceBlockResDTO {
        type = MULTIPLE_CHOICE;
    }
}
