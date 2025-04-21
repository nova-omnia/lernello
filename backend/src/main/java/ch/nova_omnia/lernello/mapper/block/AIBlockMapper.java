package ch.nova_omnia.lernello.mapper.block;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import ch.nova_omnia.lernello.dto.request.block.create.CreateAITheoryBlockDTO;
import ch.nova_omnia.lernello.dto.response.block.TheoryBlockResDTO;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AIBlockMapper {

    TheoryBlockResDTO toTheoryBlockResDTO(TheoryBlock theoryBlock);

    TheoryBlock toEntity(CreateAITheoryBlockDTO createAITheoryBlockDTO);


}
