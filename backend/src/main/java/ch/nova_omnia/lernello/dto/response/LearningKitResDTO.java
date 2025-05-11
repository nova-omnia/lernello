package ch.nova_omnia.lernello.dto.response;


import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.dto.response.user.ParticipantUserDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LearningKitResDTO(
                                @NotNull UUID uuid,
                                @NotNull @Size(min = 3, max = 40) String name,
                                List<LearningUnitResDTO> learningUnits,
                                String description,
                                ZonedDateTime deadlineDate,
                                boolean published,
                                String context,
                                List<ParticipantUserDTO> participants,
                                List<FileResDTO> files,
                                @NotNull int averageProgress,
                                @NotNull int completionRate
) {
}
