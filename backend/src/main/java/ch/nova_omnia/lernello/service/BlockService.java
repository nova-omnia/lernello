// src/main/java/ch/nova_omnia/lernello/service/BlockService.java
package ch.nova_omnia.lernello.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.dto.request.block.UpdateBlockOrderDTO;
import ch.nova_omnia.lernello.model.data.blocks.Block;
import ch.nova_omnia.lernello.repository.BlockRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;
    
    @Transactional
    public <T extends Block> T createBlock(T block, UUID learningUnitId) {
        rearrangeBlockPosition(block.getPosition(), learningUnitId);
        return blockRepository.save(block);
    }
    
   
      public List<Block> getBlocksByLearningUnit(UUID learningUnitId) {
        return blockRepository.findByLearningUnitUuidOrderByPositionAsc(learningUnitId);
    }
    
    @Transactional
    public void updateBlockOrder(UpdateBlockOrderDTO updateBlockOrderDTO) {
        List<UUID> blockIds = updateBlockOrderDTO.blockUuidsInOrder();
        for (int i = 0; i < blockIds.size(); i++) {
            Block block = blockRepository.findById(blockIds.get(i))
                .orElseThrow(() -> new RuntimeException("Block not found"));
            blockRepository.updatePosition(i, block.getUuid());
        }
    }

    private void rearrangeBlockPosition(int position, UUID learningUnitUuid) {
        List<Block> blocks = blockRepository.findByLearningUnitUuidOrderByPositionAsc(learningUnitUuid);   
        for (Block block : blocks) {
            if (block.getPosition() >= position) {
                blockRepository.updatePosition(position, block.getUuid());
            }
        }
    }
}