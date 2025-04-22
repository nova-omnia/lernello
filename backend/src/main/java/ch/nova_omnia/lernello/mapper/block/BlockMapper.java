package ch.nova_omnia.lernello.mapper.block;

import org.mapstruct.Mapper;

import ch.nova_omnia.lernello.dto.response.block.BlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.MultipleChoiceBlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.QuestionBlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.TheoryBlockResDTO;
import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.QuestionBlock;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;

@Mapper(componentModel = "spring")
public interface BlockMapper {

    MultipleChoiceBlockResDTO toMultipleChoiceBlockResDTO(MultipleChoiceBlock multipleChoiceBlock);

    TheoryBlockResDTO toTheoryBlockResDTO(TheoryBlock theoryBlock);

    QuestionBlockResDTO toQuestionBlockResDTO(QuestionBlock questionBlock);

    default BlockResDTO toBlockResDTO(Block block) {
        if (block instanceof TheoryBlock) {
            return toTheoryBlockResDTO((TheoryBlock) block);
        } else if (block instanceof MultipleChoiceBlock) {
            return toMultipleChoiceBlockResDTO((MultipleChoiceBlock) block);
        } else if (block instanceof QuestionBlock) {
            return toQuestionBlockResDTO((QuestionBlock) block);
        } else {
            throw new IllegalArgumentException("Unsupported block type: " + block.getClass().getName());
        }
    }
}