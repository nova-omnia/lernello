package ch.nova_omnia.lernello.api.block;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.block.create.AIGeneratedMultipleChoiceRequest;
import ch.nova_omnia.lernello.dto.request.block.create.AIGeneratedTheoryBlockRequest;
import ch.nova_omnia.lernello.dto.request.block.create.AIGeneratedQuestionBlockRequest;
import ch.nova_omnia.lernello.dto.response.block.MultipleChoiceBlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.TheoryBlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.QuestionBlockResDTO;
import ch.nova_omnia.lernello.mapper.block.BlockMapper;
import ch.nova_omnia.lernello.model.data.block.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.model.data.block.QuestionBlock;
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
        MultipleChoiceBlock block = aiBlockService.generateMultipleChoiceBlockAI(dto.theoryBlockId(), dto.multipleChoiceBlockId());
        return blockMapper.toMultipleChoiceBlockResDTO(block);
    }

    @PostMapping("/{id}/theoryBlock")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public TheoryBlockResDTO generateTheoryBlock(@Valid @RequestBody AIGeneratedTheoryBlockRequest dto, @PathVariable UUID id) {
        TheoryBlock theoryBlock = aiBlockService.generateTheoryBlockAI(dto.files(), dto.topic(), id);
        return blockMapper.toTheoryBlockResDTO(theoryBlock);
    }

    @PostMapping("/question")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public QuestionBlockResDTO generateQuestion(@Valid @RequestBody AIGeneratedQuestionBlockRequest dto) {
        QuestionBlock block = aiBlockService.generateQuestionBlockAI(dto.theoryBlockIUuid(), dto.questionBlockUuid());
        return blockMapper.toQuestionBlockResDTO(block);
    }

}