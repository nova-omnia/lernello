package ch.nova_omnia.lernello.dto.request.block.ai;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AIGeneratedQuestionBlockRequest(
                                              @NotNull UUID theoryBlockId,
                                              @NotNull UUID questionBlockId
) {
}