package ch.nova_omnia.lernello.dto.response;

import java.util.Map;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record TemporaryKeySolveResDTO(
                                      @NotNull Map<String, UUID> temporaryKeyMap
) {
}
