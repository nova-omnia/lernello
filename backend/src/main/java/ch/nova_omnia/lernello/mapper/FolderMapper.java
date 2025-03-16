package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ch.nova_omnia.lernello.dto.request.CreateFolderDTO;
import ch.nova_omnia.lernello.dto.response.FolderResDTO;
import ch.nova_omnia.lernello.model.data.Folder;

@Mapper(componentModel = "spring", uses = {LearningKitMapper.class, ParentFolderMapper.class})
public interface FolderMapper {
    FolderMapper INSTANCE = Mappers.getMapper(FolderMapper.class);

    FolderResDTO toDTO(Folder folder);

    Folder toEntity(CreateFolderDTO createFolderDTO);
}
