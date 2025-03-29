package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;

import ch.nova_omnia.lernello.dto.request.block.CreateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.CreateQuestionBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.dto.response.block.BlockResDTO;
import ch.nova_omnia.lernello.model.data.blocks.Block;
import ch.nova_omnia.lernello.model.data.blocks.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.blocks.QuestionBlock;
import ch.nova_omnia.lernello.model.data.blocks.TheoryBlock;

@Mapper(componentModel = "spring")
public interface BlockMapper {

    BlockResDTO toBlockResDTO(Block block);

    TheoryBlock toTheoryBlockEntity(CreateTheoryBlockDTO createTheoryBlockDTO);
    MultipleChoiceBlock toMultipleChoiceBlockEntity(CreateMultipleChoiceBlockDTO createMultipleChoiceBlock);
    QuestionBlock toQuestionBlockEntity(CreateQuestionBlockDTO createBlockDTO);
}