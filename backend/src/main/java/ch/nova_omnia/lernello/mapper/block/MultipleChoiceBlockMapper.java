package ch.nova_omnia.lernello.mapper.block;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ch.nova_omnia.lernello.dto.request.block.create.CreateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.update.UpdateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.model.data.block.MultipleChoiceBlock;

@Mapper(componentModel = "spring")
public interface MultipleChoiceBlockMapper {

    @Mapping(target = "type", constant = "MULTIPLE_CHOICE")
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "learningUnit", ignore = true)
    MultipleChoiceBlock toEntity(CreateMultipleChoiceBlockDTO createMultipleChoiceBlock);

    @Mapping(target = "type", constant = "MULTIPLE_CHOICE")
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "learningUnit", ignore = true)
    MultipleChoiceBlock toEntity(UpdateMultipleChoiceBlockDTO updateMultipleChoiceBlockDTO);
}
