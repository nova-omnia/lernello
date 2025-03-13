package ch.nova_omnia.pm4.mapper;

import ch.nova_omnia.pm4.dto.FolderDto;
import ch.nova_omnia.pm4.model.data.Folder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = { LearningKitMapper.class })
public interface FolderMapper extends GenericMapper<Folder, FolderDto> {
    FolderMapper INSTANCE = Mappers.getMapper(FolderMapper.class);

    @Mapping(target = "parent", expression = "java(DtoUtil.mapFolderParent(folder))")
    FolderDto toDto(Folder folder);
}
