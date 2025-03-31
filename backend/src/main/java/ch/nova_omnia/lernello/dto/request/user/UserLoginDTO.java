package ch.nova_omnia.lernello.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDTO(
                           @NotBlank @Email String username,
                           @NotBlank @Size(min = 8) String password) {
}
