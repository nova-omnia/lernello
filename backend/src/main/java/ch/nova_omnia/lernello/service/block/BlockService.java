// src/main/java/ch/nova_omnia/lernello/service/BlockService.java
package ch.nova_omnia.lernello.service.block;

import java.util.List;
import java.util.Optional;
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
    public Block createBlock(Block block) {
        return blockRepository.save(block);
    }

    public Optional<Block> getBlockById(UUID id) {
        return blockRepository.findById(id);
    }
      public List<Block> getBlocksByLearningUnit(UUID learningUnitId) {
        return blockRepository.findByLearningUnitIdOrderByPositionAsc(learningUnitId);
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