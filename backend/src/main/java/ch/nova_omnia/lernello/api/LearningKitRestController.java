package ch.nova_omnia.lernello.api;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ch.nova_omnia.lernello.dto.request.CreateLearningKitDTO;
import ch.nova_omnia.lernello.dto.request.UpdateLearningKitDTO;
import ch.nova_omnia.lernello.dto.response.LearningKitResDTO;
import ch.nova_omnia.lernello.mapper.LearningKitMapper;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.service.LearningKitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/learning-kits")
@Validated
@RequiredArgsConstructor
public class LearningKitRestController {
    private final LearningKitService learningKitService;
    private final LearningKitMapper learningKitMapper;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid LearningKitResDTO create(@Valid @RequestBody CreateLearningKitDTO learningKit) {
        LearningKit entity = learningKitMapper.toEntity(learningKit);
        LearningKit savedEntity = learningKitService.save(entity);
        return learningKitMapper.toDTO(savedEntity);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid LearningKitResDTO edit(@Valid @RequestBody CreateLearningKitDTO learningKit) {
        LearningKit entity = learningKitMapper.toEntity(learningKit);
        LearningKit savedEntity = learningKitService.edit(entity);
        return learningKitMapper.toDTO(savedEntity);
    }

    @DeleteMapping("/{learningKitId}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid UUID delete(@Valid @PathVariable UUID learningKitId) {
        learningKitService.deleteById(learningKitId);
        return learningKitId;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_kits:read')")
    public @Valid List<LearningKitResDTO> getAll() {
        List<LearningKit> learningKits = learningKitService.findAll();
        List<LearningKitResDTO> learningKitResDTOs = learningKits.stream().map(learningKitMapper::toDTO).collect(Collectors.toList());
        return learningKitResDTOs;
    }

    @GetMapping("/{learningKitId}")
    @PreAuthorize("hasAuthority('SCOPE_kits:read')")
    public @Valid LearningKitResDTO getById(@Valid @PathVariable UUID learningKitId) {
        return learningKitService.findById(learningKitId)
                .map(learningKitMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Learning Kit not found"));
    }

    @PutMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid LearningKitResDTO update(@Valid @RequestBody UpdateLearningKitDTO updateLearningKit) {
        LearningKit entity = learningKitMapper.toEntity(updateLearningKit);
        LearningKit savedEntity = learningKitService.update(entity, updateLearningKit.participants(), updateLearningKit.files());
        return learningKitMapper.toDTO(savedEntity);
    }
}
