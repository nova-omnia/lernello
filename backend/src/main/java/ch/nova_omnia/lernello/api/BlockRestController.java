package ch.nova_omnia.lernello.api;

import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.block.CreateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.CreateQuestionBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.UpdateBlockOrderDTO;
import ch.nova_omnia.lernello.dto.response.block.BlockResDTO;
import ch.nova_omnia.lernello.mapper.BlockMapper;
import ch.nova_omnia.lernello.service.block.BlockService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/blocks")
@RequiredArgsConstructor
public class BlockRestController {
    private final BlockService blockService;
    private final BlockMapper blockMapper;
    
    @PostMapping("/theory")
    public BlockResDTO createTheoryBlock(@RequestBody CreateTheoryBlockDTO createTheoryBlockDTO) {
        return blockMapper.toBlockResDTO(blockService.createBlock(blockMapper.toTheoryBlockEntity(createTheoryBlockDTO)));
    }
    
    @PostMapping("/multiple-choice")
    public BlockResDTO createMultipleChoiceBlock(@RequestBody CreateMultipleChoiceBlockDTO createMultipleChoiceBlockDTO) {
        return blockMapper.toBlockResDTO(blockService.createBlock(blockMapper.toMultipleChoiceBlockEntity(createMultipleChoiceBlockDTO)));
    }
    @PostMapping("/question")
    public BlockResDTO createQuestionBlock(@RequestBody CreateQuestionBlockDTO createQuestionBlockDTO) {
        return blockMapper.toBlockResDTO(blockService.createBlock(blockMapper.toQuestionBlockEntity(createQuestionBlockDTO)));
    }

    @GetMapping("/{id}")
    public Optional<BlockResDTO> getBlockById(@PathVariable UUID id) {
        return blockService.getBlockById(id).map(blockMapper::toBlockResDTO);
    }
    
    @PutMapping("/reorder")
    public void updateBlockOrder(@RequestBody UpdateBlockOrderDTO updateBlockOrderDTO) {
        blockService.updateBlockOrder(updateBlockOrderDTO);
    }
    
}