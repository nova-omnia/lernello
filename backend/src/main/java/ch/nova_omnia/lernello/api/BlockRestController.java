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

import ch.nova_omnia.lernello.dto.request.block.CreateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.CreateQuestionBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.UpdateBlockOrderDTO;
import ch.nova_omnia.lernello.dto.response.block.MultipleChoiceBlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.QuestionBlockResDTO;
import ch.nova_omnia.lernello.dto.response.block.TheoryBlockResDTO;
import ch.nova_omnia.lernello.mapper.BlockMapper;
import ch.nova_omnia.lernello.service.BlockService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/blocks")
@RequiredArgsConstructor
public class BlockRestController {
    private final BlockService blockService;
    private final BlockMapper blockMapper;

    @PostMapping("/theory")
    public TheoryBlockResDTO createTheoryBlock(@RequestBody CreateTheoryBlockDTO createTheoryBlockDTO) {
        return blockMapper.toTheoryBlockResDTO(blockService.createBlock(blockMapper.toTheoryBlockEntity(createTheoryBlockDTO), createTheoryBlockDTO.learningUnitId()));
    }

    @PostMapping("/multiple-choice")
    public MultipleChoiceBlockResDTO createMultipleChoiceBlock(@RequestBody CreateMultipleChoiceBlockDTO createMultipleChoiceBlockDTO) {
        return blockMapper.toMulitpleChoiceBlockResDTO(blockService.createBlock(blockMapper.toMultipleChoiceBlockEntity(createMultipleChoiceBlockDTO), createMultipleChoiceBlockDTO.learningUnitId()));
    }

    @PostMapping("/question")
    public QuestionBlockResDTO createQuestionBlock(@RequestBody CreateQuestionBlockDTO createQuestionBlockDTO) {
        return blockMapper.toQuestiopnBlockResDTO(blockService.createBlock(blockMapper.toQuestionBlockEntity(createQuestionBlockDTO), createQuestionBlockDTO.learningUnitId()));
    }

    @GetMapping("/learning-unit/{learningUnitId}")
    public List<?> getAllBlocks(@PathVariable UUID learningUnitId) {
        return blockService.getBlocksByLearningUnit(learningUnitId);
    }

    @PutMapping("/reorder")
    public void updateBlockOrder(@RequestBody UpdateBlockOrderDTO updateBlockOrderDTO) {
        blockService.updateBlockOrder(updateBlockOrderDTO);
    }
}