package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;

import ch.nova_omnia.lernello.dto.response.LearningUnitResDTO;
import ch.nova_omnia.lernello.model.data.LearningUnit;

@Mapper(componentModel = "spring")
public interface LearningUnitMapper {

    LearningUnitResDTO toDTO(LearningUnit learningUnit);
}
