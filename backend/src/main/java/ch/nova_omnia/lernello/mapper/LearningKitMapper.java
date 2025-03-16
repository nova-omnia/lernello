package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ch.nova_omnia.lernello.dto.LearningKitDTO;
import ch.nova_omnia.lernello.model.data.LearningKit;

@Mapper(componentModel = "spring", uses = {LearningUnitMapper.class})
public interface LearningKitMapper {
    LearningKitMapper INSTANCE = Mappers.getMapper(LearningKitMapper.class);

    LearningKitDTO toDTO(LearningKit learningKit);
}
