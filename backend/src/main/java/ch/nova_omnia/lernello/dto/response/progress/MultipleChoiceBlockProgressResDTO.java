package ch.nova_omnia.lernello.dto.response.progress;

import ch.nova_omnia.lernello.model.data.block.BlockType;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

import static ch.nova_omnia.lernello.model.data.block.BlockType.MULTIPLE_CHOICE;

public record MultipleChoiceBlockProgressResDTO(
    @NotNull
    UUID blockId,
    List<String> lastAnswers,
    Boolean isCorrect
) implements BlockProgressResDTO {
    @Override
    public BlockType blockType() {
        return MULTIPLE_CHOICE;
    }
}
