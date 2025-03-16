package ch.nova_omnia.lernello.dto.response;

import java.util.List;
import java.util.UUID;


public record SubFolderResDTO(UUID uuid, String name, List<SubFolderResDTO> subFolders) {
}
