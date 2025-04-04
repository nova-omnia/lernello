package ch.nova_omnia.lernello.dto.request.block.create;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateQuestionBlockDTO(
                                     @NotBlank String name,
                                     @Min(0) int position,
                                     @NotNull UUID learningUnitId,
                                     @NotBlank String question,
                                     @NotBlank String expectedAnswer
) implements CreateBlockDTO {

}