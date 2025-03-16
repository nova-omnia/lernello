package ch.nova_omnia.lernello.dto.response;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record SubFolderResDTO(
                              @NotNull UUID uuid,
                              @NotNull @Size(min = 3, max = 40) String name,
                              List<SubFolderResDTO> subFolders) {
}
