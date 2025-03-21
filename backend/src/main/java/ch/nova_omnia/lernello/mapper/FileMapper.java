package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import ch.nova_omnia.lernello.dto.request.UploadFileDTO;
import ch.nova_omnia.lernello.dto.response.FileResDTO;
import ch.nova_omnia.lernello.model.data.File;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    FileResDTO toDTO(File file);

    @Mapping(target = "uuid", ignore = true)
    File toEntity(UploadFileDTO uploadFileDTO);

}