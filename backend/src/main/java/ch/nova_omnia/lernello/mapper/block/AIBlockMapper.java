package ch.nova_omnia.lernello.mapper.block;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ch.nova_omnia.lernello.dto.request.block.create.CreateAITheoryBlockDTO;
import ch.nova_omnia.lernello.dto.response.block.TheoryBlockResDTO;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;

@Mapper(componentModel = "spring")
public interface AIBlockMapper {

    TheoryBlockResDTO toTheoryBlockResDTO(TheoryBlock theoryBlock);

    @Mapping(target = "type", constant = "MULTIPLE_CHOICE")
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "learningUnit", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "content", ignore = true)
    TheoryBlock toEntity(CreateAITheoryBlockDTO createAITheoryBlockDTO);


}
