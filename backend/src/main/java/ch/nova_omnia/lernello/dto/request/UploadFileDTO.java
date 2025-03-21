package ch.nova_omnia.lernello.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UploadFileDTO(@NotNull @Size(min = 1, max = 255) String name, @NotNull String filepath) {
}
