package ch.nova_omnia.lernello.dto.request.block.ai;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AIGeneratedMultipleChoiceRequest(
                                               @NotNull UUID theoryBlockIUuid,
                                               @NotNull UUID multipleChoiceBlockUuid

) {
}
