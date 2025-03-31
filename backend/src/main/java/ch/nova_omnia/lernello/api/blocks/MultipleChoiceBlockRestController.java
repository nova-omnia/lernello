package ch.nova_omnia.lernello.api.blocks;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.block.create.CreateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.update.UpdateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.dto.response.block.MultipleChoiceBlockResDTO;
import ch.nova_omnia.lernello.mapper.block.BlockMapper;
import ch.nova_omnia.lernello.mapper.block.MultipleChoiceBlockMapper;
import ch.nova_omnia.lernello.model.data.block.MultipleChoiceBlock;
import ch.nova_omnia.lernello.service.BlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/blocks")
@RequiredArgsConstructor
public class MultipleChoiceBlockRestController {
    private final BlockService blockService;
    private final BlockMapper blockMapper;
    private final MultipleChoiceBlockMapper multipleChoiceBlockMapper;

 
    @PostMapping("/multiple-choice")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public @Valid MultipleChoiceBlockResDTO createMultipleChoiceBlock(@Valid @RequestBody CreateMultipleChoiceBlockDTO createMultipleChoiceBlockDTO) {
        UUID learningUnitId = createMultipleChoiceBlockDTO.learningUnitId();
        MultipleChoiceBlock createdMultipleChoiceBlock = blockService.createBlock(multipleChoiceBlockMapper.toEntity(createMultipleChoiceBlockDTO), learningUnitId);
        return blockMapper.toMultipleChoiceBlockResDTO(createdMultipleChoiceBlock);
    }

    @PutMapping("/update/multiple-choice/")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public @Valid MultipleChoiceBlockResDTO updateMultipleChoiceBlock(@Valid @RequestBody UpdateMultipleChoiceBlockDTO updateMultipleChoiceBlockDTO) {
        MultipleChoiceBlock updatedMultipleChoiceBlock = blockService.updateMultipleChoiceBlock(multipleChoiceBlockMapper.toEntity(updateMultipleChoiceBlockDTO));
        return blockMapper.toMultipleChoiceBlockResDTO(updatedMultipleChoiceBlock);

    }


}
