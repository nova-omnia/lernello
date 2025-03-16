package ch.nova_omnia.lernello.dto.response;

import java.util.List;
import java.util.UUID;

public record InstructorResDTO(
                               UUID uuid,
                               String firstName,
                               String lastName,
                               List<LearningUnitResDTO> learningUnits
) {

}
