package ch.nova_omnia.lernello.dto.request.user;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordDataDTO(
        @NotBlank String username,
        @NotBlank String oldPassword,
        @NotBlank String newPassword,
        @NotBlank String confirmPassword) {
}