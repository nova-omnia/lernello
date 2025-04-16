package ch.nova_omnia.lernello.dto.request;

import java.time.ZonedDateTime;

import org.openapitools.jackson.nullable.JsonNullable;

import jakarta.validation.constraints.Size;


public record UpdateLearningKitDTO(
                                   @Size(min = 3, max = 40) String name,
                                   JsonNullable<String> description,
                                   JsonNullable<ZonedDateTime> deadlineDate,
                                   JsonNullable<String> context
//    Optional<List<UUID>> participants,
//    Optional<List<UUID>> files
) {

}
