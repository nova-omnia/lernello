// src/main/java/ch/nova_omnia/lernello/controller/rest/BlockController.java
package ch.nova_omnia.lernello.api;
import ch.nova_omnia.lernello.dto.request.CreateBlockDTO;
import ch.nova_omnia.lernello.dto.request.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.dto.request.UpdateBlockOrderDTO;
import ch.nova_omnia.lernello.dto.response.BlockResDTO;
import ch.nova_omnia.lernello.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/blocks")
@RequiredArgsConstructor
public class BlockRestController {
    private final BlockService blockService;
    
    @PostMapping
    public BlockResDTO createTheoryBlock(@RequestBody CreateTheoryBlockDTO createTheoryBlockDTO) {
        return blockService.createBlock(createTheoryBlockDTO);
    }
    
    @GetMapping("/learning-unit/{learningUnitId}")
    public List<BlockResDTO> getBlocksByLearningUnit(@PathVariable UUID learningUnitId) {
        return blockService.getBlocksByLearningUnit(learningUnitId);
    }
    
    @PutMapping("/reorder")
    public void updateBlockOrder(@RequestBody UpdateBlockOrderDTO updateBlockOrderDTO) {
        blockService.updateBlockOrder(updateBlockOrderDTO);
    }
    
}