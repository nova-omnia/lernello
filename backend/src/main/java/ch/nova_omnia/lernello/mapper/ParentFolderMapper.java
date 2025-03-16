package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ch.nova_omnia.lernello.dto.response.ParentFolderResDTO;
import ch.nova_omnia.lernello.model.data.Folder;

@Mapper(componentModel = "spring")
public interface ParentFolderMapper {
    ParentFolderMapper INSTANCE = Mappers.getMapper(ParentFolderMapper.class);


    ParentFolderResDTO toDTO(Folder folder);
}
