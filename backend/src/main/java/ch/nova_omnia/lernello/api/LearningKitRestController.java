package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.request.CreateLearningKitDTO;
import ch.nova_omnia.lernello.dto.request.UpdateLearningKitDTO;
import ch.nova_omnia.lernello.dto.response.LearningKitResDTO;
import ch.nova_omnia.lernello.mapper.LearningKitMapper;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.service.CustomUserDetailsService;
import ch.nova_omnia.lernello.service.LearningKitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final CustomUserDetailsService customUserDetailsService;

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
    public Page<LearningKitResDTO> getList(@AuthenticationPrincipal UserDetails userDetails, @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        UUID userId = customUserDetailsService.getUserIdByUsername(userDetails.getUsername());
        Page<LearningKit> kits = learningKitService.getList(pageable, userId);
        return kits.map(learningKitMapper::toDTO);
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

    @PostMapping("/publish/{id}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public UUID publishLearningKit(@PathVariable UUID id) {
        learningKitService.publishLearningKit(id);
        return id;
    }
}
