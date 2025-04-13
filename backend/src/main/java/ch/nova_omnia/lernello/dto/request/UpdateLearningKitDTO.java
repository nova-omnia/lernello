package ch.nova_omnia.lernello.dto.request;

import java.util.List;

import ch.nova_omnia.lernello.dto.response.FileResDTO;
import ch.nova_omnia.lernello.dto.response.user.ParticipantUserDTO;

public record UpdateLearningKitDTO(
                                   List<ParticipantUserDTO> participants,
                                   List<FileResDTO> files) {
}
