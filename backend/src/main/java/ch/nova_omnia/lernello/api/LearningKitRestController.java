package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.request.CreateLearningKitDTO;
import ch.nova_omnia.lernello.dto.request.UpdateLearningKitDTO;
import ch.nova_omnia.lernello.dto.request.UpdateLearningUnitOrderDTO;
import ch.nova_omnia.lernello.dto.request.user.CreateTraineeDTO;
import ch.nova_omnia.lernello.dto.response.LearningKitResDTO;
import ch.nova_omnia.lernello.dto.response.user.GenericSuccessDTO;
import ch.nova_omnia.lernello.mapper.LearningKitMapper;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.progress.LearningKitProgress;
import ch.nova_omnia.lernello.model.data.user.Role;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.LearningKitProgressRepository;
import ch.nova_omnia.lernello.service.CustomUserDetailsService;
import ch.nova_omnia.lernello.service.LearningKitService;
import ch.nova_omnia.lernello.service.LearningUnitService;
import ch.nova_omnia.lernello.service.UserService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/learning-kits")
@Validated
@RequiredArgsConstructor
public class LearningKitRestController {
    private final LearningKitService learningKitService;
    private final LearningUnitService learningUnitService;
    private final LearningKitMapper learningKitMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private final LearningKitProgressRepository learningKitProgressRepository;
    private final UserService userService;

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
        List<LearningKitProgress> progressesToDelete = learningKitProgressRepository.findAllByLearningKit_Uuid(id);
        if (!progressesToDelete.isEmpty()) {
            learningKitProgressRepository.deleteAll(progressesToDelete);
        }
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

        LearningKit learningKit = learningKitService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Learning Kit not found"));
        learningKitService.updateLearningKitStatistics(learningKit);
        return learningKitMapper.toDTO(learningKit);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid LearningKitResDTO updateLearningKit(@Valid @RequestBody UpdateLearningKitDTO updateLearningKit, @PathVariable UUID id) {
        LearningKit destination = learningKitService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Learning Kit not found"));
        learningKitService.updateLearningKitStatistics(destination);
        learningKitMapper.update(updateLearningKit, destination);
        LearningKit savedEntity = learningKitService.save(destination);
        return learningKitMapper.toDTO(savedEntity);
    }

    @DeleteMapping("/trainees/{kitId}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public UUID removeTraineeFromKit(@PathVariable UUID kitId, @RequestBody UUID userId) {
        learningKitService.removeTrainee(kitId, userId);
        return kitId;
    }

    @PostMapping("/publish/{id}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public UUID publishLearningKit(@PathVariable UUID id) {
        learningKitService.publishLearningKit(id);
        return id;
    }

    @PostMapping("/trainee/{id}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid UUID addTrainee(
        @PathVariable UUID id, @RequestBody @Valid CreateTraineeDTO traineeDetails
    ) {
        User trainee = userService.createUser(traineeDetails.username(), traineeDetails.name(), traineeDetails.surname(), Role.TRAINEE);
        learningKitService.saveTraineeInKit(id, trainee);
        return id;
    }

    @PatchMapping("/{id}/reorder/learning-units/")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid GenericSuccessDTO reorderLearningUnits(@PathVariable UUID id, @Valid @RequestBody UpdateLearningUnitOrderDTO updateLearningUnitOrderDTO) {
        learningUnitService.updateLearningUnitPosition(id, updateLearningUnitOrderDTO);
        return new GenericSuccessDTO(true);
    }
}
