package ch.nova_omnia.lernello.dto.response.user;

import java.time.ZonedDateTime;

import ch.nova_omnia.lernello.model.data.user.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoggedInUserDTO(
                              @NotBlank String token,
                              @NotNull ZonedDateTime expires,
                              @NotNull Role role
) {
}
