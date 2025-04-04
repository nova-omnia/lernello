package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import ch.nova_omnia.lernello.dto.request.CreateLearningUnitDTO;
import ch.nova_omnia.lernello.dto.response.LearningUnitResDTO;
import ch.nova_omnia.lernello.model.data.LearningUnit;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LearningUnitMapper {
    LearningUnitResDTO toDTO(LearningUnit learningUnit);

    LearningUnit toEntity(CreateLearningUnitDTO createLearningUnitDTO);
}
