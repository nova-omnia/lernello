package ch.nova_omnia.lernello.mapper;

import static ch.nova_omnia.lernello.model.data.block.BlockType.MULTIPLE_CHOICE;
import static ch.nova_omnia.lernello.model.data.block.BlockType.QUESTION;
import static ch.nova_omnia.lernello.model.data.block.BlockType.THEORY;

import java.util.Comparator;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import ch.nova_omnia.lernello.dto.request.CreateLearningUnitDTO;
import ch.nova_omnia.lernello.dto.response.LearningUnitResDTO;
import ch.nova_omnia.lernello.dto.response.block.BlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.MultipleChoiceBlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.QuestionBlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.TheoryBlockResDTO;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.QuestionBlock;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LearningUnitMapper {

    default LearningUnit toEntity(CreateLearningUnitDTO dto, LearningKit learningKit) {
        return new LearningUnit(dto.name(), learningKit);
    }

    default LearningUnitResDTO toDTO(LearningUnit learningUnit) {
        List<BlockResDTO> sortedBlocks = learningUnit.getBlocks().stream().sorted(Comparator.comparingInt(Block::getPosition)).map(this::mapBlockToResDTO).toList();

        return new LearningUnitResDTO(
                learningUnit.getUuid(), learningUnit.getName(), sortedBlocks
        );
    }

    default BlockResDTO mapBlockToResDTO(Block block) {

        if (block instanceof MultipleChoiceBlock) {
            return new MultipleChoiceBlockResDTO(
                    MULTIPLE_CHOICE, block.getUuid(), block.getName(), block.getPosition(), ((MultipleChoiceBlock) block).getQuestion(), ((MultipleChoiceBlock) block).getPossibleAnswers(), ((MultipleChoiceBlock) block).getCorrectAnswers()
            );
        } else if (block instanceof QuestionBlock) {
            return new QuestionBlockResDTO(
                    QUESTION, block.getUuid(), block.getName(), block.getPosition(), ((QuestionBlock) block).getQuestion(), ((QuestionBlock) block).getExpectedAnswer()
            );
        } else if (block instanceof TheoryBlock) {
            return new TheoryBlockResDTO(
                    THEORY, block.getUuid(), block.getName(), block.getPosition(), ((TheoryBlock) block).getContent()
            );
        }
        throw new IllegalArgumentException("Unknown block type: " + block.getClass().getName());
    }
}
