package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.request.progress.CheckMultipleChoiceAnswerDTO;
import ch.nova_omnia.lernello.dto.request.progress.CheckQuestionAnswerDTO;
import ch.nova_omnia.lernello.dto.request.progress.LearningKitOpened;
import ch.nova_omnia.lernello.dto.request.progress.LearningUnitOpenedDTO;
import ch.nova_omnia.lernello.dto.response.progress.LearningKitProgressResDTO;
import ch.nova_omnia.lernello.dto.response.progress.LearningUnitProgressDTO;
import ch.nova_omnia.lernello.dto.response.progress.MultipleChoiceAnswerValidationResDTO;
import ch.nova_omnia.lernello.dto.response.progress.QuestionAnswerValidationResDTO;
import ch.nova_omnia.lernello.mapper.ProgressMapper;
import ch.nova_omnia.lernello.model.data.progress.LearningKitProgress;
import ch.nova_omnia.lernello.model.data.progress.LearningUnitProgress;
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
    private final ProgressMapper progressMapper;

    @PostMapping("/learning-kit/opened")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public LearningKitProgressResDTO markLearningKitOpened(
        @RequestBody @Valid LearningKitOpened dto,
        @AuthenticationPrincipal UserDetails userDetails) {
        LearningKitProgress progress = progressService.markLearningKitOpened(dto, userDetails);

        return progressMapper.toLearningKitProgressResDTO(progress);
    }

    @PostMapping("/learning-unit/opened")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid LearningUnitProgressDTO markLearningUnitOpened(
        @RequestBody @Valid LearningUnitOpenedDTO dto,
        @AuthenticationPrincipal UserDetails userDetails) {
        LearningUnitProgress unitProgress = progressService.markLearningUnitOpened(dto, userDetails);
        return progressMapper.toLearningUnitProgressDTO(unitProgress);
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
        LearningKitProgress progress = progressService.getLearningKitProgress(learningKitId, userDetails);
        return progressMapper.toLearningKitProgressResDTO(progress);
    }

    @GetMapping("/learning-unit/{learningUnitId}")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid LearningUnitProgressDTO getLearningUnitProgress(
        @PathVariable UUID learningUnitId,
        @AuthenticationPrincipal UserDetails userDetails) {
        LearningUnitProgress progress = progressService.getLearningUnitProgress(learningUnitId, userDetails);
        return progressMapper.toLearningUnitProgressDTO(progress);
    }
}
