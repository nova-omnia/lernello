package ch.nova_omnia.lernello.dto.request;

import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;


public record UpdateLearningKitDTO(
    @Size(min = 3, max = 40) String name,
    @Size(max = 200) JsonNullable<String> description,
    JsonNullable<ZonedDateTime> deadlineDate,
    @Size(max = 200) JsonNullable<String> context,
    boolean published,
    List<UUID> files,
    List<UUID> participants
) {
}
