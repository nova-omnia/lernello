package ch.nova_omnia.lernello.dto.request.block.create;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record AIGeneratedTheoryBlockRequest(
                                      @NotBlank String topic,
                                      List<UUID> files
) {
}
