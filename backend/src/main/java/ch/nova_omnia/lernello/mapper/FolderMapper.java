package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ch.nova_omnia.lernello.dto.request.CreateFolderDTO;
import ch.nova_omnia.lernello.dto.response.folder.FolderResDTO;
import ch.nova_omnia.lernello.model.data.Folder;

@Mapper(componentModel = "spring", uses = {LearningKitMapper.class, ParentFolderMapper.class})
public interface FolderMapper {
    FolderResDTO toDTO(Folder folder);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "parentFolder", ignore = true)
    @Mapping(target = "subFolders", ignore = true)
    @Mapping(target = "learningKits", ignore = true)
    Folder toEntity(CreateFolderDTO createFolderDTO);
}
