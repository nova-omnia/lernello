package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import ch.nova_omnia.lernello.dto.request.CreateLearningKitDTO;
import ch.nova_omnia.lernello.dto.request.UpdateLearningKitDTO;
import ch.nova_omnia.lernello.dto.response.LearningKitResDTO;
import ch.nova_omnia.lernello.model.data.LearningKit;

@Mapper(componentModel = "spring", uses = {LearningUnitMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LearningKitMapper {
    LearningKitResDTO toDTO(LearningKit learningKit);

    LearningKit toEntity(CreateLearningKitDTO createLearningKitResDto);

    @Mapping(source = "learningKitId", target = "uuid")
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "files", ignore = true)
    LearningKit toEntity(UpdateLearningKitDTO updateLearningKitDTO);
}
