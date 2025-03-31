package ch.nova_omnia.lernello.api.blocks;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.block.create.CreateQuestionBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.update.UpdateQuestionBlockDTO;
import ch.nova_omnia.lernello.dto.response.block.QuestionBlockResDTO;
import ch.nova_omnia.lernello.mapper.block.BlockMapper;
import ch.nova_omnia.lernello.mapper.block.QuestionBlockMapper;
import ch.nova_omnia.lernello.model.data.block.QuestionBlock;
import ch.nova_omnia.lernello.service.BlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Validated
@RestController
@RequestMapping("/api/blocks")
@RequiredArgsConstructor
public class QuestionBlockRestController {
    private final BlockService blockService;
    private final BlockMapper blockMapper;
    private final QuestionBlockMapper questionBlockMapper;


    @PostMapping("/question")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public @Valid QuestionBlockResDTO createQuestionBlock(@Valid @RequestBody CreateQuestionBlockDTO createQuestionBlockDTO) {
        UUID learningUnitId = createQuestionBlockDTO.learningUnitId();
        QuestionBlock createdQuestionBlock = blockService.createBlock(questionBlockMapper.toEntity(createQuestionBlockDTO), learningUnitId);
        return blockMapper.toQuestionBlockResDTO(createdQuestionBlock);
    }

    @PutMapping("/update/question")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public @Valid QuestionBlockResDTO updateQuestionBlock(@Valid @RequestBody UpdateQuestionBlockDTO updateQuestionBlockDTO) {
        QuestionBlock questionBlock = blockService.updateQuestionBlock(questionBlockMapper.toEntity(updateQuestionBlockDTO));
        return blockMapper.toQuestionBlockResDTO(questionBlock);
    }


}
