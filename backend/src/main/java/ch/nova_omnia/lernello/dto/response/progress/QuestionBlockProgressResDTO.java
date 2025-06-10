package ch.nova_omnia.lernello.dto.response.progress;

import ch.nova_omnia.lernello.model.data.block.BlockType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record QuestionBlockProgressResDTO(
    @NotNull
    UUID blockId,
    String lastAnswer,
    Boolean isCorrect
) implements BlockProgressResDTO {
    @Override
    public BlockType blockType() {
        return BlockType.QUESTION;
    }
}
