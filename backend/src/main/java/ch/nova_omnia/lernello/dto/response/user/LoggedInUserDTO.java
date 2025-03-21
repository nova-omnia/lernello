package ch.nova_omnia.lernello.dto.response.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoggedInUserDTO(
        @NotBlank String token,
        @NotBlank String username,
        @NotBlank boolean changedPassword,
        @NotNull Integer expires) {
}
