package ch.nova_omnia.lernello.dto.response.user;

import java.util.UUID;

import ch.nova_omnia.lernello.model.data.user.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserInfoDTO(
                          String locale,
                          @NotBlank String username,
                          @NotNull UUID uuid,
                          boolean changedPassword,
                          @NotNull Role role
) {
}
