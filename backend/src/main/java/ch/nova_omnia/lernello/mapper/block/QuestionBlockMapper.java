package ch.nova_omnia.lernello.mapper.block;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ch.nova_omnia.lernello.dto.request.block.create.CreateQuestionBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.update.UpdateQuestionBlockDTO;
import ch.nova_omnia.lernello.model.data.block.QuestionBlock;

@Mapper(componentModel = "spring")
public interface QuestionBlockMapper {

    @Mapping(target = "type", constant = "QUESTION")
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "learningUnit.uuid", source = "learningUnitId")
    QuestionBlock toEntity(CreateQuestionBlockDTO createQuestionBlockDTO);

    @Mapping(target = "type", constant = "QUESTION")
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "learningUnit", ignore = true)
    QuestionBlock toEntity(UpdateQuestionBlockDTO updateQuestionBlockDTO);


}
