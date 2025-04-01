package ch.nova_omnia.lernello.api.block;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.block.create.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.dto.request.block.update.UpdateTheoryBlockDTO;
import ch.nova_omnia.lernello.dto.response.block.TheoryBlockResDTO;
import ch.nova_omnia.lernello.mapper.block.BlockMapper;
import ch.nova_omnia.lernello.mapper.block.TheoryBlockMapper;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.service.BlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/theory-block")
@RequiredArgsConstructor
public class TheoryBlockRestController {
    private final BlockService blockService;
    private final BlockMapper blockMapper;
    private final TheoryBlockMapper theoryBlockMapper;


    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public @Valid TheoryBlockResDTO createTheoryBlock(@Valid @RequestBody CreateTheoryBlockDTO createTheoryBlockDTO) {
        UUID learningUnitId = createTheoryBlockDTO.learningUnitId();
        TheoryBlock createdTheoryBlock = blockService.createBlock(theoryBlockMapper.toEntity(createTheoryBlockDTO), learningUnitId);
        return blockMapper.toTheoryBlockResDTO(createdTheoryBlock);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('SCOPE_blocks:write')")
    public @Valid TheoryBlockResDTO updateTheoryBlock(@Valid @RequestBody UpdateTheoryBlockDTO updateTheoryBlockDTO) {
        TheoryBlock theoryBlock = blockService.updateTheoryBlock(theoryBlockMapper.toEntity(updateTheoryBlockDTO));
        return blockMapper.toTheoryBlockResDTO(theoryBlock);
    }

}
