package ch.nova_omnia.lernello.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDataDTO(
                                    @NotBlank @Size(min = 8) String newPassword) {
}
