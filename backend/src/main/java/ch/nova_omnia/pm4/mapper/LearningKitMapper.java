package ch.nova_omnia.pm4.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ch.nova_omnia.pm4.dto.LearningKitDTO;
import ch.nova_omnia.pm4.model.data.LearningKit;

@Mapper(componentModel = "spring")
public interface LearningKitMapper {

    // Map the parentFolder.id field to parentFolderId in the DTO.
    @Mapping(source = "parentFolder.id", target = "parentFolderId")
    LearningKitDTO toDto(LearningKit learningKit);

    // When mapping from DTO to entity, ignore the association.
    // You can set it later in your service after retrieving the Folder entity.
    @Mapping(target = "parentFolder", ignore = true)
    LearningKit toEntity(LearningKitDTO dto);




}
