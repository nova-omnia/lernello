package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;

import ch.nova_omnia.lernello.dto.response.FileResDTO;
import ch.nova_omnia.lernello.model.data.File;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileResDTO toDTO(File file);
}