package ch.nova_omnia.lernello.dto.request;

import java.util.List;
import java.util.UUID;

public record GenerateLearningUnitDTO(
                                      List<UUID> fileIds
) {
}
