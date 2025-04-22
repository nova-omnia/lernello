package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.request.CreateLearningKitDTO;
import ch.nova_omnia.lernello.dto.request.UpdateLearningKitDTO;
import ch.nova_omnia.lernello.dto.response.LearningKitResDTO;
import ch.nova_omnia.lernello.mapper.LearningKitMapper;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.service.LearningKitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/learning-kits")
@Validated
@RequiredArgsConstructor
public class LearningKitRestController {
    private final LearningKitService learningKitService;
    private final LearningKitMapper learningKitMapper;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid LearningKitResDTO create(@Valid @RequestBody CreateLearningKitDTO learningKit) {
        LearningKit entity = learningKitMapper.toEntity(learningKit);
        LearningKit savedEntity = learningKitService.save(entity);
        return learningKitMapper.toDTO(savedEntity);
    }

    @PatchMapping("/{learningKitId}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid LearningKitResDTO update(@PathVariable UUID learningKitId, @Valid @RequestBody UpdateLearningKitDTO updateLearningKit) {
        LearningKit destination = learningKitService.findById(learningKitId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Learning Kit not found"));
        learningKitMapper.update(updateLearningKit, destination);
        LearningKit savedEntity = learningKitService.save(destination);
        return learningKitMapper.toDTO(savedEntity);
    }

    @DeleteMapping("/{learningKitId}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public UUID delete(@PathVariable UUID learningKitId) {
        learningKitService.deleteById(learningKitId);
        return learningKitId;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_kits:read')")
    public Page<LearningKitResDTO> getList(@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<LearningKit> kits = learningKitService.getList(pageable);
        return kits.map(learningKitMapper::toDTO);
    }

    @GetMapping("/{learningKitId}")
    @PreAuthorize("hasAuthority('SCOPE_kits:read')")
    public @Valid LearningKitResDTO getById(@Valid @PathVariable UUID learningKitId) {
        return learningKitService.findById(learningKitId).map(learningKitMapper::toDTO).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Learning Kit not found"));
    }


    @DeleteMapping("/participants/{kitId}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public UUID removeParticipantFromKit(@PathVariable UUID kitId, @RequestBody UUID userId) {
        learningKitService.removeParticipant(kitId, userId);
        return kitId;
    }
}
