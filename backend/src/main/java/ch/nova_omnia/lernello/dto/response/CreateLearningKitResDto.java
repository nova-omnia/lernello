package ch.nova_omnia.lernello.dto.response;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateLearningKitResDto (
                                @NotNull UUID uuid) {
}
