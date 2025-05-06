package ch.nova_omnia.lernello.dto.request.user;

import ch.nova_omnia.lernello.model.data.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CreateUserDTO(
    @NotNull @Email String username,
    @NotNull String name,
    @NotNull String surname,
    @NotNull Role role
) {
}
