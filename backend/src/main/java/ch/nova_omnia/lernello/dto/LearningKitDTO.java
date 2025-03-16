package ch.nova_omnia.lernello.dto;

import java.util.List;
import java.util.UUID;

public record LearningKitDTO(
                             UUID uuid,
                             String name,
                             List<LearningUnitDTO> learningUnits) {

}
