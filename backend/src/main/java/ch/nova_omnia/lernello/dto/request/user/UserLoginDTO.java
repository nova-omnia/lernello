package ch.nova_omnia.lernello.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserLoginDTO(
                           @NotNull @Size(min = 3, max = 40) String username,
                           @NotBlank @Size(min = 8) String password
) {
}
