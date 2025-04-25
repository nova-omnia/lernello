package ch.nova_omnia.lernello.api.block;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.block.create.AIGeneratedMultipleChoiceRequest;
import ch.nova_omnia.lernello.dto.response.block.MultipleChoiceBlockResDTO;
import ch.nova_omnia.lernello.mapper.block.BlockMapper;
import ch.nova_omnia.lernello.model.data.block.MultipleChoiceBlock;
import ch.nova_omnia.lernello.service.AIBlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai-block")
@RequiredArgsConstructor
public class AIBlockRestController {

    private final AIBlockService aiBlockService;
    private final BlockMapper blockMapper;

    @PostMapping("/multiple-choice")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public MultipleChoiceBlockResDTO generateMultipleChoice(@Valid @RequestBody AIGeneratedMultipleChoiceRequest dto) {
        MultipleChoiceBlock block = aiBlockService.generateMultipleChoiceBlockAI(dto.theoryBlockIUuid(), dto.learningUnitId(), dto.multipleChoiceBlockUuid());
        return blockMapper.toMultipleChoiceBlockResDTO(block);
    }
}