package ch.nova_omnia.lernello.dto.request;

import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.dto.response.FileResDTO;
import ch.nova_omnia.lernello.dto.response.user.ParticipantUserDTO;
import lombok.Data;

@Data
public class UpdateLearningKitDTO extends CreateLearningKitDTO {
    List<UUID> participants;
    List<UUID> files;
}
