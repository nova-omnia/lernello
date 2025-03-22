package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ch.nova_omnia.lernello.dto.request.UploadMediaFileDTO;
import ch.nova_omnia.lernello.dto.response.MediaFileResDTO;
import ch.nova_omnia.lernello.model.data.MediaFile;

@Mapper(componentModel = "spring")
public interface MediaFileMapper {
    MediaFileResDTO toDTO(MediaFile file);

    @Mapping(target = "uuid", ignore = true)
    MediaFile toEntity(UploadMediaFileDTO uploadFileDTO);

}