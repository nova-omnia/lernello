package ch.nova_omnia.lernello.mapper;

import ch.nova_omnia.lernello.dto.response.progress.*;
import ch.nova_omnia.lernello.model.data.progress.LearningKitProgress;
import ch.nova_omnia.lernello.model.data.progress.LearningUnitProgress;
import ch.nova_omnia.lernello.model.data.progress.block.BlockProgress;
import ch.nova_omnia.lernello.model.data.progress.block.TheoryBlockProgress;
import ch.nova_omnia.lernello.model.data.progress.block.scorable.MultipleChoiceBlockProgress;
import ch.nova_omnia.lernello.model.data.progress.block.scorable.QuestionBlockProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProgressMapper {

    @Mapping(source = "learningKit.uuid", target = "learningKitId")
    @Mapping(source = "user.uuid", target = "userId")
    @Mapping(source = "opened", target = "isOpened")
    @Mapping(source = "completed", target = "isCompleted")
    LearningKitProgressResDTO toLearningKitProgressResDTO(LearningKitProgress learningKitProgress);

    @Mapping(source = "learningUnit.uuid", target = "learningUnitId")
    @Mapping(source = "opened", target = "isOpened")
    @Mapping(source = "completed", target = "isCompleted")
    LearningUnitProgressResDTO toLearningUnitProgressDTO(LearningUnitProgress learningUnitProgress);

    @Mapping(source = "block.uuid", target = "blockId")
    @Mapping(source = "isViewed", target = "isViewed")
    TheoryBlockViewedResDTO toTheoryBlockViewedResDTO(TheoryBlockProgress theoryBlockProgress);

    default BlockProgressResDTO toBlockProgressResDTO(BlockProgress blockProgress) {
        if (blockProgress == null) {
            return null;
        }
        if (blockProgress instanceof TheoryBlockProgress theoryBlockProgress) {
            return toTheoryBlockProgressResDTO(theoryBlockProgress);
        } else if (blockProgress instanceof QuestionBlockProgress questionBlockProgress) {
            return toQuestionBlockProgressResDTO(questionBlockProgress);
        } else if (blockProgress instanceof MultipleChoiceBlockProgress multipleChoiceBlockProgress) {
            return toMultipleChoiceBlockProgressResDTO(multipleChoiceBlockProgress);
        } else {
            throw new IllegalArgumentException("Unknown block progress type: " + blockProgress.getClass());
        }
    }

    @Mapping(source = "block.uuid", target = "blockId")
    MultipleChoiceBlockProgressResDTO toMultipleChoiceBlockProgressResDTO(MultipleChoiceBlockProgress multipleChoiceBlockProgress);

    @Mapping(source = "block.uuid", target = "blockId")
    QuestionBlockProgressResDTO toQuestionBlockProgressResDTO(QuestionBlockProgress questionBlockProgress);

    @Mapping(source = "block.uuid", target = "blockId")
    TheoryBlockProgressResDTO toTheoryBlockProgressResDTO(TheoryBlockProgress theoryBlockProgress);
}
