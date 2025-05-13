package ch.nova_omnia.lernello.dto.response.user;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record ParticipantUserDTO(
                                 @NotNull UUID uuid,
                                 @NotNull String username,
                                 @NotNull String name,
                                 @NotNull String surname
) {
}
