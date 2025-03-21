package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;

import ch.nova_omnia.lernello.dto.response.ParentFolderResDTO;
import ch.nova_omnia.lernello.model.data.Folder;

@Mapper(componentModel = "spring")
public interface ParentFolderMapper {


    ParentFolderResDTO toDTO(Folder folder);
}
