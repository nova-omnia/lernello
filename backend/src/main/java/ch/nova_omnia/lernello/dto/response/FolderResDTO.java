package ch.nova_omnia.lernello.dto.response;

import java.util.List;
import java.util.UUID;

public record FolderResDTO(
                           UUID uuid,
                           String name,
                           ParentFolderResDTO parentFolder,
                           List<SubFolderResDTO> subFolders,
                           List<LearningKitResDTO> learningKits
) {
}
