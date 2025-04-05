package ch.nova_omnia.lernello.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record CreateLearningKitDTO(
        UUID uuid,
        @NotNull @Size(min = 3, max = 40) String name,
        String description,
        LocalDate deadlineDate,
        UUID folderId,
        String context
    ){
}
