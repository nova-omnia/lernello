package ch.nova_omnia.lernello.dto.response;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FolderResDTO(
                           @NotNull UUID uuid,
                           @NotNull @Size(min = 3, max = 40) String name,
                           ParentFolderResDTO parentFolder,
                           List<SubFolderResDTO> subFolders,
                           List<LearningKitResDTO> learningKits
) {
}
