package ch.nova_omnia.lernello.dto.response.user;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserDTO(
    @NotNull UUID uuid,
    @NotNull String username,
    @NotNull String name,
    @NotNull String surname
) {
}
