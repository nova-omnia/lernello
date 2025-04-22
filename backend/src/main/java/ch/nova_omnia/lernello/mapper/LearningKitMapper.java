package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import ch.nova_omnia.lernello.dto.request.CreateLearningKitDTO;
import ch.nova_omnia.lernello.dto.request.UpdateLearningKitDTO;
import ch.nova_omnia.lernello.dto.response.LearningKitResDTO;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.service.UserService;
import ch.nova_omnia.lernello.service.file.FileService;

@Mapper(
        componentModel = "spring", uses = {LearningUnitMapper.class, JsonNullableMapper.class, FileService.class, UserService.class
        }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LearningKitMapper {
    LearningKitResDTO toDTO(LearningKit learningKit);

    LearningKit toEntity(CreateLearningKitDTO createLearningKitResDto);

    void update(UpdateLearningKitDTO source, @MappingTarget LearningKit destination);
}
