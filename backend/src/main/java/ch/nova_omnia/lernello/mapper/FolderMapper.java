package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ch.nova_omnia.lernello.dto.FolderDTO;
import ch.nova_omnia.lernello.model.data.Folder;

@Mapper(componentModel = "spring", uses = {LearningKitMapper.class, ParentFolderMapper.class})
public interface FolderMapper {
    FolderMapper INSTANCE = Mappers.getMapper(FolderMapper.class);


    FolderDTO toDTO(Folder folder);
}
