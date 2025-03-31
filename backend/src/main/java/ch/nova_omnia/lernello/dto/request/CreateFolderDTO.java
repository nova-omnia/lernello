package ch.nova_omnia.lernello.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateFolderDTO(@NotNull @Size(min = 3, max = 40) String name) {
}
