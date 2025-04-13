package ch.nova_omnia.lernello.dto.request;

import java.time.ZonedDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateLearningKitDTO {
    @NotNull @Size(min = 3, max = 40) String name;
    String description;
    ZonedDateTime deadlineDate;
    String context;
}
