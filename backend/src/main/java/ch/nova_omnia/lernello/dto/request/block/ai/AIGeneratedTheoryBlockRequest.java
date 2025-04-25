package ch.nova_omnia.lernello.dto.request.block.ai;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AIGeneratedTheoryBlockRequest(
                                            @NotNull UUID blockId,
                                            @NotBlank String topic,
                                            List<UUID> files
) {
}
