package ch.nova_omnia.pm4.mapper;

import ch.nova_omnia.pm4.dto.LearningKitDto;
import ch.nova_omnia.pm4.model.data.LearningKit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = { LearningUnitMapper.class })
public interface LearningKitMapper extends GenericMapper<LearningKit, LearningKitDto> {
    LearningKitMapper INSTANCE = Mappers.getMapper(LearningKitMapper.class);

    @Mapping(target = "parent", expression = "java(DtoUtil.toSimpleDto(learningKit.getParentFolder()))")
    LearningKitDto toDto(LearningKit learningKit);
}
