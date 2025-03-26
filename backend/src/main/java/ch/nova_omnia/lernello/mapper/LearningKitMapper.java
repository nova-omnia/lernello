package ch.nova_omnia.lernello.mapper;

import ch.nova_omnia.lernello.dto.request.CreateLerningKitDTO;
import org.mapstruct.Mapper;

import ch.nova_omnia.lernello.dto.response.LearningKitResDTO;
import ch.nova_omnia.lernello.model.data.LearningKit;

@Mapper(componentModel = "spring", uses = {LearningUnitMapper.class})
public interface LearningKitMapper {
    LearningKitResDTO toDTO(LearningKit learningKit);

    LearningKit toEntity(CreateLerningKitDTO createLerningKitDTO);
}
