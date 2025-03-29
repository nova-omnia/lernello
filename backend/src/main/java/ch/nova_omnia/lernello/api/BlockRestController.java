package ch.nova_omnia.lernello.api;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.block.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.UpdateBlockOrderDTO;
import ch.nova_omnia.lernello.dto.response.block.BlockResDTO;
import ch.nova_omnia.lernello.service.block.BlockService;
import lombok.RequiredArgsConstructor;


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