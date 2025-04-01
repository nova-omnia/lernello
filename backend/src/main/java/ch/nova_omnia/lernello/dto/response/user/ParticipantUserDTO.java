package ch.nova_omnia.lernello.dto.response.user;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ParticipantUserDTO(@NotNull UUID uuid, @NotNull String username) {
}
