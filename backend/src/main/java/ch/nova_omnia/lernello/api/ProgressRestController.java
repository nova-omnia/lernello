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
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public LearningKitProgressResDTO markLearningKitOpened(
        @RequestBody @Valid LearningKitOpened dto,
        @AuthenticationPrincipal UserDetails userDetails) {
        return progressService.markLearningKitOpened(dto, userDetails);
    }

    @PostMapping("/learning-unit/opened")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid LearningUnitProgressDTO markLearningUnitOpened(
        @RequestBody @Valid LearningUnitOpenedDTO dto,
        @AuthenticationPrincipal UserDetails userDetails) {
        return progressService.markLearningUnitOpened(dto, userDetails);
    }

    @PostMapping("/block/multiple-choice/check")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid MultipleChoiceAnswerValidationResDTO checkMultipleChoiceAnswer(
        @RequestBody @Valid CheckMultipleChoiceAnswerDTO dto,
        @AuthenticationPrincipal UserDetails userDetails) {
        return progressService.checkMultipleChoiceAnswer(dto, userDetails);
    }

    @PostMapping("/block/question/check")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid QuestionAnswerValidationResDTO checkQuestionAnswer(
        @RequestBody @Valid CheckQuestionAnswerDTO dto,
        @AuthenticationPrincipal UserDetails userDetails) {
        return progressService.checkQuestionAnswer(dto, userDetails);
    }

    @GetMapping("/learning-kit/{learningKitId}")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid LearningKitProgressResDTO getLearningKitProgress(
        @PathVariable UUID learningKitId,
        @AuthenticationPrincipal UserDetails userDetails) {
        return progressService.getLearningKitProgress(learningKitId, userDetails);
    }

    @GetMapping("/learning-unit/{learningUnitId}")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid LearningUnitProgressDTO getLearningUnitProgress(
        @PathVariable UUID learningUnitId,
        @AuthenticationPrincipal UserDetails userDetails) {
        return progressService.getLearningUnitProgress(learningUnitId, userDetails);
    }
}
