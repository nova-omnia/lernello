package ch.nova_omnia.lernello.dto.response.folder;

import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.dto.response.LearningKitResDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FolderResDTO(
                           @NotNull UUID uuid,
                           @NotBlank @Size(min = 3, max = 40) String name,
                           ParentFolderResDTO parentFolder,
                           List<SubFolderResDTO> subFolders,
                           List<LearningKitResDTO> learningKits) {
}
