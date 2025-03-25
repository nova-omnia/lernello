package ch.nova_omnia.lernello.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ChangePasswordDataDTO(
        @NotNull UUID uuid,
        @NotBlank @Size(min = 8) String oldPassword,
        @NotBlank @Size(min = 8) String newPassword,
        @NotBlank @Size(min = 8) String confirmPassword) {
}