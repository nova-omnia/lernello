package ch.nova_omnia.lernello.mapper.block;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ch.nova_omnia.lernello.dto.request.block.create.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.update.UpdateTheoryBlockDTO;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;

@Mapper(componentModel = "spring")
public interface TheoryBlockMapper {

    @Mapping(target = "type", constant = "THEORY")
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "learningUnit.uuid", source = "learningUnitId")
    TheoryBlock toEntity(CreateTheoryBlockDTO createTheoryBlockDTO);

    @Mapping(target = "type", constant = "THEORY")
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "learningUnit", ignore = true)
    TheoryBlock toEntity(UpdateTheoryBlockDTO updateTheoryBlockDTO);


}
