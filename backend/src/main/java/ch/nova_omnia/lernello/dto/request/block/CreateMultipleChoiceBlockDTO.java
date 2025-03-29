package ch.nova_omnia.lernello.dto.request.block;

import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.model.data.blocks.Block.BlockType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateMultipleChoiceBlockDTO(
                                           @NotBlank String name,
                                           @NotNull @Min(0) int position,
                                           @NotNull BlockType blockType,
                                           @NotNull UUID learningUnitId,
                                           @NotNull String question,
                                           @NotNull List<String> possibleAnswers,
                                           @NotNull List<String> correctAnswers) {
}
