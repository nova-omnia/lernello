package ch.nova_omnia.lernello.api.block;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.block.update.UpdateBlockOrderDTO;
import ch.nova_omnia.lernello.dto.response.block.BlockResDTO;
import ch.nova_omnia.lernello.mapper.block.BlockMapper;
import ch.nova_omnia.lernello.service.BlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/blocks")
@RequiredArgsConstructor
public class BlockRestController {
    private final BlockService blockService;
    private final BlockMapper blockMapper;

    @GetMapping("/learning-unit/{learningUnitId}")
    @PreAuthorize("hasAuthority('SCOPE_blocks:read')")
    public @Valid List<BlockResDTO> getAllBlocks(@PathVariable UUID learningUnitId) {
        return blockService.getBlocksByLearningUnit(learningUnitId).stream().map(blockMapper::toBlockResDTO).toList();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public void deleteById(@PathVariable UUID blockId) {
        blockService.deleteBlockById(blockId);
    }

    @PutMapping("/reorder")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public void updateBlockOrder(@Valid @RequestBody UpdateBlockOrderDTO updateBlockOrderDTO) {
        blockService.updateBlockOrder(updateBlockOrderDTO);
    }
}