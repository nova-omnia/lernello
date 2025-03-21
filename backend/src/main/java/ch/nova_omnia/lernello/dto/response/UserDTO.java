package ch.nova_omnia.lernello.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDTO(
                      @NotBlank String token,
                      @NotBlank String username,
                      @NotNull Integer expires) {
}
