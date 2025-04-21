package ch.nova_omnia.lernello.api.block;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.block.create.CreateAITheoryBlockDTO;
import ch.nova_omnia.lernello.dto.response.block.TheoryBlockResDTO;
import ch.nova_omnia.lernello.mapper.block.AIBlockMapper;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.service.AIBlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai-block")
@RequiredArgsConstructor
public class AIBlockRestController {

    private final AIBlockService aiBlockService;
    private final AIBlockMapper aiBlockMapper;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public TheoryBlockResDTO generateTheoryBlock(@Valid @RequestBody CreateAITheoryBlockDTO createAITheoryBlockDTO, @PathVariable UUID learningUnitId) {
        List<UUID> fileIds = createAITheoryBlockDTO.fileIds();
        TheoryBlock createdTheoryBlock = aiBlockService.createAiTheoryBlock(aiBlockMapper.toEntity(createAITheoryBlockDTO), learningUnitId, fileIds);
        return aiBlockMapper.toTheoryBlockResDTO(createdTheoryBlock);
    }
}