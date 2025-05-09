package ch.nova_omnia.lernello.dto.request.user;

import ch.nova_omnia.lernello.model.data.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
    @NotNull @Email String username,
    @Size(min = 3, max = 40)
    @NotNull String name,
    @Size(min = 3, max = 40)
    @NotNull String surname,
    @NotNull Role role
) {
}
