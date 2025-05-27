package ch.nova_omnia.lernello.dto.request;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.openapitools.jackson.nullable.JsonNullable;

import jakarta.validation.constraints.Size;


public record UpdateLearningKitDTO(
                                   @Size(min = 3, max = 40) String name,
                                   @Size(max = 200) JsonNullable<String> description,
                                   JsonNullable<ZonedDateTime> deadlineDate,
                                   boolean published,
                                   List<UUID> files,
                                   List<UUID> trainees
) {
}
