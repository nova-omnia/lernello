package ch.nova_omnia.lernello.dto.response.user;

import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoggedInUserDTO(
                              @NotNull UUID uuid,
                              @NotBlank String token,
                              boolean changedPassword,
                              @NotNull ZonedDateTime expires) {
}
