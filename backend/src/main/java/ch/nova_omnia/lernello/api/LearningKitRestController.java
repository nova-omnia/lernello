package ch.nova_omnia.lernello.api;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid LearningKitResDTO createLearningKit(@Valid @RequestBody CreateLearningKitDTO learningKit) {
        LearningKit entity = learningKitMapper.toEntity(learningKit);
        LearningKit savedEntity = learningKitService.save(entity);
        return learningKitMapper.toDTO(savedEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public UUID deleteLearningKit(@PathVariable UUID id) {
        learningKitService.deleteById(id);
        return id;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_kits:read')")
    public @Valid List<LearningKitResDTO> getAllLearningKits() {
        List<LearningKit> learningKits = learningKitService.findAll();
        List<LearningKitResDTO> learningKitResDTOs = learningKits.stream().map(learningKitMapper::toDTO).collect(Collectors.toList());
        return learningKitResDTOs;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_kits:read')")
    public @Valid LearningKitResDTO getLearningKitById(@Valid @PathVariable UUID id) {
        return learningKitService.findById(id).map(learningKitMapper::toDTO).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Learning Kit not found"));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid LearningKitResDTO updateLearningKit(@Valid @RequestBody UpdateLearningKitDTO updateLearningKit, @PathVariable UUID id) {
        LearningKit destination = learningKitService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Learning Kit not found"));
        learningKitMapper.update(updateLearningKit, destination);
        LearningKit savedEntity = learningKitService.save(destination);
        return learningKitMapper.toDTO(savedEntity);
    }


    @DeleteMapping("/participants/{kitId}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public UUID removeParticipantFromKit(@PathVariable UUID kitId, @RequestBody UUID userId) {
        learningKitService.removeParticipant(kitId, userId);
        return kitId;
    }
}
