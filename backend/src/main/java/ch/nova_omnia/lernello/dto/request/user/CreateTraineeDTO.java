package ch.nova_omnia.lernello.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CreateTraineeDTO(
                                   @NotNull @Email String username,
                                   @NotNull String name,
                                   @NotNull String surname
) {
}
