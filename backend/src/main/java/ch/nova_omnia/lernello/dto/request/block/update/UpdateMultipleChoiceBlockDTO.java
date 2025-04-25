package ch.nova_omnia.lernello.dto.request.block.update;

import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.model.data.block.BlockType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static ch.nova_omnia.lernello.model.data.block.BlockType.MULTIPLE_CHOICE;

public record UpdateMultipleChoiceBlockDTO(
                                           @NotNull BlockType type,
                                           @NotNull UUID uuid,
                                           @NotBlank String name,
                                           @NotBlank String question,
                                           @NotNull List<String> possibleAnswers,
                                           @NotNull List<String> correctAnswers
) implements UpdateBlockDTO {
    public UpdateMultipleChoiceBlockDTO {
        type = MULTIPLE_CHOICE;
    }
}
