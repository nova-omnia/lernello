package ch.nova_omnia.lernello.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import ch.nova_omnia.lernello.dto.request.block.CreateBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.dto.response.block.BlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.TheoryBlockResDTO;
import ch.nova_omnia.lernello.model.data.blocks.Block;
import ch.nova_omnia.lernello.model.data.blocks.TheoryBlock;
import ch.nova_omnia.lernello.service.block.BlockFactory;

@Mapper(componentModel = "spring")
public abstract class BlockMapper {

    @Autowired
    protected BlockFactory blockFactory;

    @Mapping(target = "learningUnit", ignore = true)
    public Block toEntity(CreateBlockDTO createBlockDTO) {
        return blockFactory.createBlock(createBlockDTO);
    }

    @Mapping(target = "learningUnit", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    public abstract TheoryBlock toTheoryBlock(CreateTheoryBlockDTO createTheoryBlockDTO);

    public abstract BlockResDTO toBlockResDTO(Block block);

    @Mapping(target = "content", source = "content")
    public abstract TheoryBlockResDTO toTheoryBlockResDTO(TheoryBlock theoryBlock);

    public abstract List<BlockResDTO> toBlockResDTOList(List<Block> blocks);
}