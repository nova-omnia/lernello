package ch.nova_omnia.lernello.dto.response.user;

import ch.nova_omnia.lernello.model.data.user.Role;
import jakarta.validation.constraints.NotNull;

public record UserRoleDTO(
    @NotNull Role role
)  {
}
