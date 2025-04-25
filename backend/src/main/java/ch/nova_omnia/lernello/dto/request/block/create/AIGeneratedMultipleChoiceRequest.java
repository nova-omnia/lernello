package ch.nova_omnia.lernello.dto.request.block.create;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AIGeneratedMultipleChoiceRequest(
                                               @NotNull UUID theoryBlockId,
                                               @NotNull UUID multipleChoiceBlockId

) {
}
