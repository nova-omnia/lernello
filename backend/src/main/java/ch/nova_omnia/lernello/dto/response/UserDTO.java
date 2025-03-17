package ch.nova_omnia.lernello.dto.response;

import jakarta.validation.constraints.NotNull;

public record UserDTO(@NotNull String token) {
}
