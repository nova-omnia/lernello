package ch.nova_omnia.lernello.dto;

import java.util.List;
import java.util.UUID;

public record InstructorDTO(
                            UUID uuid,
                            String firstName,
                            String lastName,
                            List<LearningUnitDTO> learningUnits
) {

}
