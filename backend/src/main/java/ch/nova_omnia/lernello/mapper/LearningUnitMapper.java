package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ch.nova_omnia.lernello.dto.response.LearningUnitResDTO;
import ch.nova_omnia.lernello.model.data.LearningUnit;

@Mapper(componentModel = "spring")
public interface LearningUnitMapper {
    LearningUnitMapper INSTANCE = Mappers.getMapper(LearningUnitMapper.class);

    LearningUnitResDTO toDTO(LearningUnit learningUnit);
}
