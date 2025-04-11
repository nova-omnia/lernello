package ch.nova_omnia.lernello.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ch.nova_omnia.lernello.dto.request.CreateLearningUnitDTO;
import ch.nova_omnia.lernello.dto.request.blockActions.BlockActionDTO;
import ch.nova_omnia.lernello.dto.response.LearningUnitResDTO;
import ch.nova_omnia.lernello.mapper.LearningUnitMapper;
import ch.nova_omnia.lernello.mapper.TemporaryKeyMapper;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.service.LearningUnitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/learning-unit")
@Validated
@RequiredArgsConstructor
public class LearningUnitRestController {
    private final LearningUnitService learningUnitService;
    private final LearningUnitMapper learningUnitMapper;
    private final TemporaryKeyMapper temporaryKeyMapper;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SCOPE_learningUnit:write')")
    public @Valid LearningUnitResDTO createLearningUnit(@Valid @RequestBody CreateLearningUnitDTO createLearningUnitDTO) {
        LearningUnit newLearningUnit = learningUnitMapper.toEntity(createLearningUnitDTO);
        LearningUnit learningUnit = learningUnitService.createLearningUnit(newLearningUnit);
        return learningUnitMapper.toDTO(learningUnit);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_learningUnit:write')")
    public void deleteById(@PathVariable UUID id) {
        learningUnitService.deleteById(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_learningUnit:read')")
    public @Valid LearningUnitResDTO getById(@PathVariable UUID id) {
        return learningUnitService.findById(id).map(learningUnitMapper::toDTO).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_learningUnit:read')")
    public List<@Valid LearningUnitResDTO> getAllLearningUnits() {
        return learningUnitService.findAll().stream().map(learningUnitMapper::toDTO).toList();
    }

    @PostMapping("/{id}/apply-block-actions")
    @PreAuthorize("hasAuthority('SCOPE_learningUnit:write')")
    public @Valid Map<String, UUID> applyBlockActions(@PathVariable UUID id, @RequestBody List<BlockActionDTO> actionQueue) {
        Map<String, UUID> temporaryKeyMap = learningUnitService.applyBlockActions(id, actionQueue);
        return temporaryKeyMapper.toDTO(temporaryKeyMap).temporaryKeyMap();
    }
}
