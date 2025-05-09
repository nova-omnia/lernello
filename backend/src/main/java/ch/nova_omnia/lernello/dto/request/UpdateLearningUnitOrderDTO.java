package ch.nova_omnia.lernello.dto.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record UpdateLearningUnitOrderDTO(
                                         @NotNull List<UUID> learningUnitUuidsInOrder
) {
}
