package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ch.nova_omnia.lernello.dto.ParentFolderDTO;
import ch.nova_omnia.lernello.model.data.Folder;

@Mapper(componentModel = "spring")
public interface ParentFolderMapper {
    ParentFolderMapper INSTANCE = Mappers.getMapper(ParentFolderMapper.class);


    ParentFolderDTO toDTO(Folder folder);
}
