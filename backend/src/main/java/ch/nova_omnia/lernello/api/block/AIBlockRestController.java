package ch.nova_omnia.lernello.api.block;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.block.ai.AIGeneratedQuestionBlockRequest;
import ch.nova_omnia.lernello.dto.request.block.ai.AIGeneratedTheoryBlockRequest;
import ch.nova_omnia.lernello.dto.response.block.MultipleChoiceBlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.QuestionBlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.TheoryBlockResDTO;
import ch.nova_omnia.lernello.mapper.block.BlockMapper;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.QuestionBlock;
import ch.nova_omnia.lernello.service.block.AIBlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai-block")
@RequiredArgsConstructor
@Validated
public class AIBlockRestController {

    private final AIBlockService aiBlockService;
    private final BlockMapper blockMapper;

    @PostMapping("/multiple-choice")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public MultipleChoiceBlockResDTO generateMultipleChoice(@Valid @RequestBody AIGeneratedQuestionBlockRequest dto) {
        MultipleChoiceBlock block = aiBlockService.generateMultipleChoiceBlockAI(dto.blockId(), dto.theoryBlockId());
        return blockMapper.toMultipleChoiceBlockResDTO(block);
    }

    @PostMapping("/theory")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public TheoryBlockResDTO generateTheory(@Valid @RequestBody AIGeneratedTheoryBlockRequest dto) {
        TheoryBlock theoryBlock = aiBlockService.generateTheoryBlockAI(dto.blockId(), dto.files(), dto.topic());
        return blockMapper.toTheoryBlockResDTO(theoryBlock);
    }


    @PostMapping("/question-block")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")

    public QuestionBlockResDTO generateQuestion(@Valid @RequestBody AIGeneratedQuestionBlockRequest dto) {
        QuestionBlock block = aiBlockService.generateQuestionBlockAI(dto.blockId(), dto.theoryBlockId());
        return blockMapper.toQuestionBlockResDTO(block);
    }
}
