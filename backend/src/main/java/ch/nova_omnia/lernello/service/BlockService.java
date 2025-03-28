// src/main/java/ch/nova_omnia/lernello/service/BlockService.java
package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.dto.request.CreateBlockDTO;
import ch.nova_omnia.lernello.dto.request.UpdateBlockOrderDTO;
import ch.nova_omnia.lernello.dto.response.BlockResDTO;
import ch.nova_omnia.lernello.mapper.BlockMapper;
import ch.nova_omnia.lernello.model.data.blocks.Block;
import ch.nova_omnia.lernello.repository.BlockRepository;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ch.nova_omnia.lernello.dto.request.CreateTheoryBlockDTO;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;
    private final LearningUnitRepository learningUnitRepository;
    private final BlockMapper blockMapper;
    
    @Transactional
    public BlockResDTO createBlock(CreateBlockDTO createBlockDTO) {
        Block block;
        
        if (createBlockDTO instanceof CreateTheoryBlockDTO) {
            block = blockMapper.toTheoryBlock((CreateTheoryBlockDTO) createBlockDTO);
        } else {
            block = blockMapper.toEntity(createBlockDTO);
        }
        
        block.setLearningUnit(learningUnitRepository.findById(createBlockDTO.getLearningUnitId())
            .orElseThrow(() -> new RuntimeException("LearningUnit not found")));
            
        Block savedBlock = blockRepository.save(block);
        return blockMapper.toBlockResDTO(savedBlock);
    }
    
    public List<BlockResDTO> getBlocksByLearningUnit(UUID learningUnitId) {
        return blockMapper.toBlockResDTOList(
            blockRepository.findByLearningUnitIdOrderByPositionAsc(learningUnitId)
        );
    }
    
    @Transactional
    public void updateBlockOrder(UpdateBlockOrderDTO updateBlockOrderDTO) {
        List<UUID> blockIds = updateBlockOrderDTO.getBlockUuidsInOrder();
        for (int i = 0; i < blockIds.size(); i++) {
            Block block = blockRepository.findById(blockIds.get(i))
                .orElseThrow(() -> new RuntimeException("Block not found"));
            block.setPosition(i);
            blockRepository.save(block);
        }
    }
}