package ch.nova_omnia.lernello.dto.response.user;

import ch.nova_omnia.lernello.model.data.user.Role;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserResDTO(
    @NotNull UUID uuid,
    @NotNull String username,
    @NotNull String name,
    @NotNull String surname,
    @NotNull Role role
) {
}
