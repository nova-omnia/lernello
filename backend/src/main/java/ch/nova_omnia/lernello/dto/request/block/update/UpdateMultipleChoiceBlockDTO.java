package ch.nova_omnia.lernello.dto.request.block.update;

import static ch.nova_omnia.lernello.model.data.block.BlockType.MULTIPLE_CHOICE;

import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.model.data.block.BlockType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateMultipleChoiceBlockDTO(
                                           @NotNull BlockType type,
                                           @NotNull UUID uuid,
                                           @NotBlank String name,
                                           String question,
                                           List<String> possibleAnswers,
                                           List<String> correctAnswers
) implements UpdateBlockDTO {
    public UpdateMultipleChoiceBlockDTO {
        type = MULTIPLE_CHOICE;
    }
}
