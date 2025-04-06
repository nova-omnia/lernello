package ch.nova_omnia.lernello.dto.response.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LoggedInUserDTO(
        @NotNull UUID uuid,
        @NotBlank String token,
        String locale,
        boolean changedPassword,
        @NotNull int expires) {
}
