package ch.nova_omnia.lernello.api.block;

import org.springframework.security.access.prepost.PreAuthorize;
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
import ch.nova_omnia.lernello.model.data.block.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.QuestionBlock;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
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
    public MultipleChoiceBlockResDTO generateMultipleChoice(@Valid @RequestBody AIGeneratedQuestionBlockRequest dto) {
        MultipleChoiceBlock block = aiBlockService.generateMultipleChoiceBlockAI(dto.theoryBlockId(), dto.questionBlockId());
        return blockMapper.toMultipleChoiceBlockResDTO(block);
    }

    @PostMapping("/theory")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public TheoryBlockResDTO generateTheory(@Valid @RequestBody AIGeneratedTheoryBlockRequest dto) {
        TheoryBlock theoryBlock = aiBlockService.generateTheoryBlockAI(dto.files(), dto.topic(), dto.blockId());
        return blockMapper.toTheoryBlockResDTO(theoryBlock);
    }


    @PostMapping("/questionBlock")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public QuestionBlockResDTO generateQuestion(@Valid @RequestBody AIGeneratedQuestionBlockRequest dto) {
        QuestionBlock block = aiBlockService.generateQuestionBlockAI(dto.theoryBlockId(), dto.questionBlockId());
        return blockMapper.toQuestionBlockResDTO(block);
    }
}