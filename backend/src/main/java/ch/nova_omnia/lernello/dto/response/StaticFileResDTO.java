package ch.nova_omnia.lernello.dto.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StaticFileResDTO(
                               @NotNull @Size(min = 1) String fileUrl
) {
}
