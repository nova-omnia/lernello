package ch.nova_omnia.pm4.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ch.nova_omnia.pm4.dto.LearningUnitDTO;
import ch.nova_omnia.pm4.model.data.LearningUnit;

@Mapper(componentModel = "spring")
public interface LearningUnitMapper {

    // Map the parentLearningKit.id field to learningKitId in the DTO.
    @Mapping(source = "parentLearningKit.id", target = "learningKitId")
    LearningUnitDTO toDto(LearningUnit learningUnit);

    // When mapping from DTO to entity, ignore the association.
    // You can set it later in your service after retrieving the LearningKit entity.
    @Mapping(target = "parentLearningKit", ignore = true)
    LearningUnit toEntity(LearningUnitDTO dto);
}