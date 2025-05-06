package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.request.progress.CheckMultipleChoiceAnswerDTO;
import ch.nova_omnia.lernello.dto.request.progress.CheckQuestionAnswerDTO;
import ch.nova_omnia.lernello.dto.request.progress.LearningKitOpened;
import ch.nova_omnia.lernello.dto.request.progress.LearningUnitOpenedDTO;
import ch.nova_omnia.lernello.dto.response.progress.LearningKitProgressResDTO;
import ch.nova_omnia.lernello.dto.response.progress.LearningUnitProgressDTO;
import ch.nova_omnia.lernello.dto.response.progress.MultipleChoiceAnswerValidationResDTO;
import ch.nova_omnia.lernello.dto.response.progress.QuestionAnswerValidationResDTO;
import ch.nova_omnia.lernello.service.ProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/progress")
@Validated
public class ProgressRestController {
    private final ProgressService progressService;

    @PostMapping("/learning-kit/opened")
    @PreAuthorize("hasAuthority('SCOPE_progress:write') or hasAuthority('ROLE_TRAINEE') or hasAuthority('ROLE_EXPERT')")
    public ResponseEntity<LearningKitProgressResDTO> markLearningKitOpened(
        @RequestBody @Valid LearningKitOpened dto,
        @AuthenticationPrincipal UserDetails userDetails) {
        LearningKitProgressResDTO response = progressService.markLearningKitOpened(dto, userDetails);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/learning-unit/opened")
    @PreAuthorize("hasAuthority('SCOPE_progress:write') or hasAuthority('ROLE_TRAINEE') or hasAuthority('ROLE_EXPERT')")
    public ResponseEntity<LearningUnitProgressDTO> markLearningUnitOpened(
        @RequestBody @Valid LearningUnitOpenedDTO dto,
        @AuthenticationPrincipal UserDetails userDetails) {
        LearningUnitProgressDTO response = progressService.markLearningUnitOpened(dto, userDetails);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/block/multiple-choice/check")
    @PreAuthorize("hasAuthority('SCOPE_progress:write') or hasAuthority('ROLE_TRAINEE') or hasAuthority('ROLE_EXPERT')")
    public ResponseEntity<MultipleChoiceAnswerValidationResDTO> checkMultipleChoiceAnswer(
        @RequestBody @Valid CheckMultipleChoiceAnswerDTO dto,
        @AuthenticationPrincipal UserDetails userDetails) {
        MultipleChoiceAnswerValidationResDTO response = progressService.checkMultipleChoiceAnswer(dto, userDetails);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/block/question/check")
    @PreAuthorize("hasAuthority('SCOPE_progress:write') or hasAuthority('ROLE_TRAINEE') or hasAuthority('ROLE_EXPERT')")
    public ResponseEntity<QuestionAnswerValidationResDTO> checkQuestionAnswer(
        @RequestBody @Valid CheckQuestionAnswerDTO dto,
        @AuthenticationPrincipal UserDetails userDetails) {
        QuestionAnswerValidationResDTO response = progressService.checkQuestionAnswer(dto, userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/learning-kit/{learningKitId}")
    @PreAuthorize("hasAuthority('SCOPE_progress:read') or hasAuthority('ROLE_TRAINEE') or hasAuthority('ROLE_EXPERT')")
    public ResponseEntity<LearningKitProgressResDTO> getLearningKitProgress(
        @PathVariable UUID learningKitId,
        @AuthenticationPrincipal UserDetails userDetails) {
        LearningKitProgressResDTO response = progressService.getLearningKitProgress(learningKitId, userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/learning-unit/{learningUnitId}")
    @PreAuthorize("hasAuthority('SCOPE_progress:read') or hasAuthority('ROLE_TRAINEE') or hasAuthority('ROLE_EXPERT')")
    public ResponseEntity<LearningUnitProgressDTO> getLearningUnitProgress(
        @PathVariable UUID learningUnitId,
        @AuthenticationPrincipal UserDetails userDetails) {
        LearningUnitProgressDTO response = progressService.getLearningUnitProgress(learningUnitId, userDetails);
        return ResponseEntity.ok(response);
    }
}
