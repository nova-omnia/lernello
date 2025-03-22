package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import ch.nova_omnia.lernello.dto.request.UploadFileDTO;
import ch.nova_omnia.lernello.dto.response.MediaFileResDTO;
import ch.nova_omnia.lernello.model.data.MediaFile;

@Mapper(componentModel = "spring")
public interface MediaFileMapper {
    MediaFileMapper INSTANCE = Mappers.getMapper(MediaFileMapper.class);

    MediaFileResDTO toDTO(MediaFile file);

    @Mapping(target = "uuid", ignore = true)
    MediaFile toEntity(UploadFileDTO uploadFileDTO);

}