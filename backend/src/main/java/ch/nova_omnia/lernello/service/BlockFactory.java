package ch.nova_omnia.lernello.service;
import org.springframework.stereotype.Component;

import ch.nova_omnia.lernello.dto.request.CreateBlockDTO;
import ch.nova_omnia.lernello.dto.request.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.model.data.blocks.Block;
import ch.nova_omnia.lernello.model.data.blocks.TheoryBlock;

@Component
public class BlockFactory {
    
    public Block createBlock(CreateBlockDTO dto) {
        if (dto instanceof CreateTheoryBlockDTO) {
            CreateTheoryBlockDTO theoryDto = (CreateTheoryBlockDTO) dto;
            return new TheoryBlock(
                theoryDto.getName(),
                theoryDto.getPosition(),
                theoryDto.getContent(),
                dto.getBlockType(),
                null
            );
        }
        throw new IllegalArgumentException("Unsupported block type: " + dto.getBlockType());
    }
}