package ch.nova_omnia.pm4.mapper;

import ch.nova_omnia.pm4.dto.LearningUnitDto;
import ch.nova_omnia.pm4.model.data.LearningUnit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LearningUnitMapper extends GenericMapper<LearningUnit, LearningUnitDto> {
    LearningUnitMapper INSTANCE = Mappers.getMapper(LearningUnitMapper.class);

    @Mapping(target = "parent", expression = "java(DtoUtil.toSimpleDto(learningUnit.getLearningKit()))")
    LearningUnitDto toDto(LearningUnit learningUnit);
}
