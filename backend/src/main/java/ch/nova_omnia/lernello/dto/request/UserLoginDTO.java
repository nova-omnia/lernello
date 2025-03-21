package ch.nova_omnia.lernello.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
        @NotBlank String username,
        @NotBlank String password) {
}
