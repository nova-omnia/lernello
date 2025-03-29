package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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

    MultipleChoiceBlockResDTO toMultipleChoiceBlockResDTO(MultipleChoiceBlock multipleChoiceBlock);

    QuestionBlockResDTO toQuestionBlockResDTO(QuestionBlock questionBlock);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "learningUnit.uuid", source = "learningUnitId")
    TheoryBlock toTheoryBlockEntity(CreateTheoryBlockDTO createTheoryBlockDTO);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "learningUnit.uuid", source = "learningUnitId")
    MultipleChoiceBlock toMultipleChoiceBlockEntity(CreateMultipleChoiceBlockDTO createMultipleChoiceBlock);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "learningUnit.uuid", source = "learningUnitId")
    QuestionBlock toQuestionBlockEntity(CreateQuestionBlockDTO createBlockDTO);
}