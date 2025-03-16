package ch.nova_omnia.lernello.dto;

import java.util.List;
import java.util.UUID;

public record FolderDTO(
                        UUID uuid,
                        String name,
                        ParentFolderDTO parentFolder,
                        List<SubFolderDTO> subFolders,
                        List<LearningKitDTO> learningKits
) {
}
