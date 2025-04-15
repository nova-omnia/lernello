package ch.nova_omnia.lernello.dto.request;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class UpdateLearningKitDTO extends CreateLearningKitDTO {
    UUID learningKitId;
    List<UUID> participants;
    List<UUID> files;
}
