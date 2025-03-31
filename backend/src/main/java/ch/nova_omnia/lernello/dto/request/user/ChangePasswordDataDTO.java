package ch.nova_omnia.lernello.dto.request.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordDataDTO(
                                    @NotBlank @Min(8) String newPassword) {
}
