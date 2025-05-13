package ch.nova_omnia.lernello.dto.response.user;

import java.time.ZonedDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoggedInUserDTO(
                              @NotBlank String token,
                              @NotNull ZonedDateTime expires
) {
}
