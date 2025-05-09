package ch.nova_omnia.lernello.dto.request;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.NotNull;

public record UpdateLearningUnitOrderDTO(
                                         @NotNull @JsonDeserialize(contentAs = UUID.class) List<UUID> learningUnitUuidsInOrder
) {
}
