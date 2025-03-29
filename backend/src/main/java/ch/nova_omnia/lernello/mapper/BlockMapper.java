package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;

import ch.nova_omnia.lernello.dto.request.block.CreateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.CreateQuestionBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.dto.response.block.MultipleChoiceBlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.QuestionBlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.TheoryBlockResDTO;
import ch.nova_omnia.lernello.model.data.blocks.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.blocks.QuestionBlock;
import ch.nova_omnia.lernello.model.data.blocks.TheoryBlock;

@Mapper(componentModel = "spring")
public interface BlockMapper {

    TheoryBlockResDTO toTheoryBlockResDTO(TheoryBlock theoryBlock);

    MultipleChoiceBlockResDTO toMulitpleChoiceBlockResDTO(MultipleChoiceBlock multipleChoiceBlock);

    QuestionBlockResDTO toQuestiopnBlockResDTO(QuestionBlock questionBlock);

    TheoryBlock toTheoryBlockEntity(CreateTheoryBlockDTO createTheoryBlockDTO);

    MultipleChoiceBlock toMultipleChoiceBlockEntity(CreateMultipleChoiceBlockDTO createMultipleChoiceBlock);

    QuestionBlock toQuestionBlockEntity(CreateQuestionBlockDTO createBlockDTO);
}