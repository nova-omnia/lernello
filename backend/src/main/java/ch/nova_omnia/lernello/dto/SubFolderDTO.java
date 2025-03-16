package ch.nova_omnia.lernello.dto;

import java.util.List;
import java.util.UUID;


public record SubFolderDTO(UUID uuid, String name, List<SubFolderDTO> subFolders) {
}
