package ch.nova_omnia.lernello.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;


public record CreateLearningKitDTO(
    @NotNull @Size(min = 3, max = 40) String name,
    @Size(max = 200) String description,
    ZonedDateTime deadlineDate,
    @Size(max = 200) String context
) {
}
