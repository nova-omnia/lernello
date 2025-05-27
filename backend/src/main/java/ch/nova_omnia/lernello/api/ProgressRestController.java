package ch.nova_omnia.lernello.api;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.progress.CheckMultipleChoiceAnswerDTO;
import ch.nova_omnia.lernello.dto.request.progress.CheckQuestionAnswerDTO;
import ch.nova_omnia.lernello.dto.request.progress.LearningKitOpened;
import ch.nova_omnia.lernello.dto.request.progress.LearningUnitOpenedDTO;
import ch.nova_omnia.lernello.dto.request.progress.TheoryBlockViewedDTO;
import ch.nova_omnia.lernello.dto.response.progress.LearningKitProgressResDTO;
import ch.nova_omnia.lernello.dto.response.progress.LearningUnitProgressResDTO;
import ch.nova_omnia.lernello.dto.response.progress.MultipleChoiceAnswerValidationResDTO;
import ch.nova_omnia.lernello.dto.response.progress.QuestionAnswerValidationResDTO;
import ch.nova_omnia.lernello.dto.response.progress.TheoryBlockViewedResDTO;
import ch.nova_omnia.lernello.mapper.ProgressMapper;
import ch.nova_omnia.lernello.model.data.progress.LearningKitProgress;
import ch.nova_omnia.lernello.model.data.progress.LearningUnitProgress;
import ch.nova_omnia.lernello.model.data.progress.block.TheoryBlockProgress;
import ch.nova_omnia.lernello.service.ProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
                                                           @RequestBody @Valid LearningKitOpened dto, @AuthenticationPrincipal UserDetails userDetails) {
        LearningKitProgress progress = progressService.markLearningKitOpened(dto, userDetails);
        return progressMapper.toLearningKitProgressResDTO(progress);
    }

    @PostMapping("/learning-unit/opened")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid LearningUnitProgressResDTO markLearningUnitOpened(
                                                                 @RequestBody @Valid LearningUnitOpenedDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        LearningUnitProgress unitProgress = progressService.markLearningUnitOpened(dto, userDetails);
        return progressMapper.toLearningUnitProgressDTO(unitProgress);
    }

    @PostMapping("/block/multiple-choice/check")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid MultipleChoiceAnswerValidationResDTO checkMultipleChoiceAnswer(
                                                                                 @RequestBody @Valid CheckMultipleChoiceAnswerDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        return progressService.checkMultipleChoiceAnswer(dto, userDetails);
    }

    @PostMapping("/block/question/check")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid QuestionAnswerValidationResDTO checkQuestionAnswer(
                                                                     @RequestBody @Valid CheckQuestionAnswerDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        return progressService.checkQuestionAnswer(dto, userDetails);
    }

    @PostMapping("/block/theory/viewed")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid TheoryBlockViewedResDTO markTheoryBlockViewed(
                                                                @RequestBody @Valid TheoryBlockViewedDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        TheoryBlockProgress theoryBlockProgress = progressService.markTheoryBlockViewed(dto, userDetails);
        return progressMapper.toTheoryBlockViewedResDTO(theoryBlockProgress);
    }

    @GetMapping("/learning-kit/{learningKitId}")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid LearningKitProgressResDTO getLearningKitProgress(
                                                                   @PathVariable UUID learningKitId, @AuthenticationPrincipal UserDetails userDetails) {
        LearningKitProgress progress = progressService.getLearningKitProgress(learningKitId, userDetails);
        return progressMapper.toLearningKitProgressResDTO(progress);
    }

    @GetMapping("/learning-unit/{learningUnitId}")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid LearningUnitProgressResDTO getLearningUnitProgress(
                                                                  @PathVariable UUID learningUnitId, @AuthenticationPrincipal UserDetails userDetails) {
        LearningUnitProgress progress = progressService.getLearningUnitProgress(learningUnitId, userDetails);
        return progressMapper.toLearningUnitProgressDTO(progress);
    }

    @GetMapping("/learning-kit/{learningKitId}/trainees-progress")
    @PreAuthorize("hasAuthority('SCOPE_kits:read')")
    public @Valid List<LearningKitProgressResDTO> getLearningKitProgressForAllTrainees(
                                                                                           @PathVariable UUID learningKitId) {
        List<LearningKitProgress> progresses = progressService.getLearningKitProgressForAllTrainees(learningKitId);
        return progresses.stream().map(progressMapper::toLearningKitProgressResDTO).collect(Collectors.toList());
    }
}
