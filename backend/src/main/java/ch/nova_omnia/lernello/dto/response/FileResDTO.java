package ch.nova_omnia.lernello.dto.response;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FileResDTO(@NotNull UUID uuid, @NotNull @Size(min = 1, max = 255) String name, @NotNull String filepath) {
}
