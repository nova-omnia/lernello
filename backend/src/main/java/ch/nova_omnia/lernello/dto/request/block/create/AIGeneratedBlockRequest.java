package ch.nova_omnia.lernello.dto.request.block.create;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AIGeneratedBlockRequest(
                                      @NotNull UUID fileId,
                                      @NotBlank String topic,
                                      int position,
                                      @NotNull UUID learningUnitId
) {
}
