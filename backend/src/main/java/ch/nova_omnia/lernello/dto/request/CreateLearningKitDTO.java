package ch.nova_omnia.lernello.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateLearningKitDTO(
                                   @NotNull @Size(min = 3, max = 40) String name,
                                   String description,
                                   LocalDate deadlineDate,
                                   String context
) {
}
