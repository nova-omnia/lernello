package ch.nova_omnia.lernello.dto.request.block.update;

import static ch.nova_omnia.lernello.model.data.block.BlockType.QUESTION;

import java.util.UUID;

import ch.nova_omnia.lernello.model.data.block.BlockType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateQuestionBlockDTO(
                                     @NotNull BlockType type,
                                     @NotNull UUID uuid,
                                     @NotBlank String name,
                                     String question,
                                     String expectedAnswer
) implements UpdateBlockDTO {
    public UpdateQuestionBlockDTO {
        type = QUESTION;
    }
}
