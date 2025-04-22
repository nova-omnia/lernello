package ch.nova_omnia.lernello.dto.request.block.create;

import java.util.UUID;

import ch.nova_omnia.lernello.dto.response.block.TheoryBlockResDTO;
import jakarta.validation.constraints.NotNull;

public record AIGeneratedMultipleChoiceRequest(
                                      @NotNull TheoryBlockResDTO theoryBlock,
                                      @NotNull UUID learningUnitId
) {
}
