package ch.nova_omnia.lernello.dto.request.block.ai;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AIGeneratedTheoryBlockRequest(
                                            String topic,
                                            @NotNull List<UUID> files,
                                            @NotNull UUID blockId
) {
}
