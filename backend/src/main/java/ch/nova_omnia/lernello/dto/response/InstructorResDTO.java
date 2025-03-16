package ch.nova_omnia.lernello.dto.response;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InstructorResDTO(
                               @NotNull UUID uuid,
                               @NotNull @Size(min = 3, max = 40) String firstName,
                               @NotNull @Size(min = 3, max = 40) String lastName
) {

}
