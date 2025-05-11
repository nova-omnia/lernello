package ch.nova_omnia.lernello.dto.request;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public record UpdateLearningUnitOrderDTO(
                                         @NotNull @JsonProperty("learningUnitUuidsInOrder") List<UUID> learningUnitUuidsInOrder
) {
}
