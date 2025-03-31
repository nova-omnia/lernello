package ch.nova_omnia.lernello.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.nova_omnia.lernello.service.LearningUnitService;
import ch.nova_omnia.lernello.dto.request.CreateLearningUnitDTO;
import ch.nova_omnia.lernello.dto.response.LearningUnitResDTO;
import ch.nova_omnia.lernello.mapper.LearningUnitMapper;

@RestController
@RequestMapping("/api/learning-kit/learning-unit")
@Validated
@RequiredArgsConstructor
public class LearningUnitRestController {

    private final LearningUnitService learningUnitService;
    private final LearningUnitMapper learningUnitMapper;

    @PostMapping( "/create")
    @PreAuthorize("hasAuthority('SCOPE_folders:write')")
    public @Valid LearningUnitResDTO createLearningUnit(@Valid @RequestBody CreateLearningUnitDTO createLearningUnitDTO) {
        return learningUnitMapper.toDTO(learningUnitService.createLearningUnit(learningUnitMapper.toEntity(createLearningUnitDTO)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_files:write')")
    public void deleteById(@PathVariable UUID id) {
        learningUnitService.deleteById(id);
    }

    @GetMapping( "/{id}")
    @PreAuthorize("hasAuthority('SCOPE_folders:read')")
    public @Valid Optional<LearningUnitResDTO> getById(@PathVariable UUID id) {
        return learningUnitService.findById(id).map(learningUnitMapper::toDTO);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('SCOPE_folders:read')")
    public List<@Valid LearningUnitResDTO> getAllLearningUnits() {
        return learningUnitService.findAll().stream().map(learningUnitMapper::toDTO).toList();
    }

    @PutMapping("/{id}/blocks/order")
    @PreAuthorize("hasAuthority('SCOPE_folders:write')")
    public @Valid LearningUnitResDTO updateBlockOrder(@PathVariable UUID id, @RequestBody List<UUID> blockIds) {
        return learningUnitMapper.toDTO(learningUnitService.updateBlockOrder(id, blockIds).orElseThrow());
    }
}