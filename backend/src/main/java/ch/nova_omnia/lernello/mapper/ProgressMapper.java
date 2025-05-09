package ch.nova_omnia.lernello.mapper;

import ch.nova_omnia.lernello.dto.response.progress.LearningKitProgressResDTO;
import ch.nova_omnia.lernello.dto.response.progress.LearningUnitProgressDTO;
import ch.nova_omnia.lernello.dto.response.progress.TheoryBlockViewedResDTO;
import ch.nova_omnia.lernello.model.data.progress.LearningKitProgress;
import ch.nova_omnia.lernello.model.data.progress.LearningUnitProgress;
import ch.nova_omnia.lernello.model.data.progress.block.TheoryBlockProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProgressMapper {

    @Mapping(source = "learningKit.uuid", target = "learningKitId")
    @Mapping(source = "opened", target = "isOpened")
    @Mapping(source = "completed", target = "isCompleted")
    LearningKitProgressResDTO toLearningKitProgressResDTO(LearningKitProgress learningKitProgress);

    @Mapping(source = "learningUnit.uuid", target = "learningUnitId")
    @Mapping(source = "opened", target = "isOpened")
    @Mapping(source = "completed", target = "isCompleted")
    LearningUnitProgressDTO toLearningUnitProgressDTO(LearningUnitProgress learningUnitProgress);


    @Mapping(source = "block.uuid", target = "blockId")
    TheoryBlockViewedResDTO toTheoryBlockViewedResDTO(TheoryBlockProgress theoryBlockProgress);
}
