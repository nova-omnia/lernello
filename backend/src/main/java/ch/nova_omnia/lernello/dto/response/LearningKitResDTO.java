package ch.nova_omnia.lernello.dto.response;

import java.util.List;
import java.util.UUID;

public record LearningKitResDTO(
                                UUID uuid,
                                String name,
                                List<LearningUnitResDTO> learningUnits) {
}
