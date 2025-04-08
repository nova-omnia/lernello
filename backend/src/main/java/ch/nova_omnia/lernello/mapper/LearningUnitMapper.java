package ch.nova_omnia.lernello.mapper;

import ch.nova_omnia.lernello.model.data.block.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Named;

import ch.nova_omnia.lernello.dto.request.CreateLearningUnitDTO;
import ch.nova_omnia.lernello.dto.response.LearningUnitResDTO;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.dto.response.block.BlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.MultipleChoiceBlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.QuestionBlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.TheoryBlockResDTO;

import java.util.List;
import java.util.stream.Collectors;

import static ch.nova_omnia.lernello.model.data.block.BlockType.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LearningUnitMapper {

    LearningUnitResDTO toDTO(LearningUnit learningUnit);

    LearningUnit toEntity(CreateLearningUnitDTO createLearningUnitDTO);

    @Named("mapBlocks")
    default List<BlockResDTO> mapBlocks(List<Block> blocks) {
        if (blocks == null) {
            return null;
        }
        return blocks.stream()
                .map(this::mapBlockToResDTO)
                .collect(Collectors.toList());
    }

    default BlockResDTO mapBlockToResDTO(Block block) {

        if (block instanceof MultipleChoiceBlock) {
            return new MultipleChoiceBlockResDTO(
                    MULTIPLE_CHOICE,
                    block.getUuid(),
                    block.getName(),
                    block.getPosition(),
                    ((MultipleChoiceBlock) block).getQuestion(),
                    ((MultipleChoiceBlock) block).getPossibleAnswers(),
                    ((MultipleChoiceBlock) block).getCorrectAnswers()
            );
        } else if (block instanceof QuestionBlock) {
            return new QuestionBlockResDTO(
                    QUESTION,
                    block.getUuid(),
                    block.getName(),
                    block.getPosition(),
                    ((QuestionBlock) block).getQuestion(),
                    ((QuestionBlock) block).getExpectedAnswer()
            );
        } else if (block instanceof TheoryBlock) {
            return new TheoryBlockResDTO(
                    THEORY,
                    block.getUuid(),
                    block.getName(),
                    block.getPosition(),
                    ((TheoryBlock) block).getContent()
            );
        }
        throw new IllegalArgumentException("Unknown block type: " + block.getClass().getName());
    }
}