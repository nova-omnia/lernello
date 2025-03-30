package ch.nova_omnia.lernello.dto.request.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDTO(
        @NotBlank @Size(min = 3, max = 40) String username,
        @NotBlank @Min(8) String password
) {
}
