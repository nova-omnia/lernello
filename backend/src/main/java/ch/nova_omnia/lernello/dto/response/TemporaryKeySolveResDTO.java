package ch.nova_omnia.lernello.dto.response;

import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record TemporaryKeySolveResDTO(
        @NotNull
        Map<String, UUID> temporaryKeyMap) {
}
