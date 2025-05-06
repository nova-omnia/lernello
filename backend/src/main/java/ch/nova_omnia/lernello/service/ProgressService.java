package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.dto.request.progress.CheckMultipleChoiceAnswerDTO;
import ch.nova_omnia.lernello.dto.request.progress.CheckQuestionAnswerDTO;
import ch.nova_omnia.lernello.dto.request.progress.LearningKitOpened;
import ch.nova_omnia.lernello.dto.request.progress.LearningUnitOpenedDTO;
import ch.nova_omnia.lernello.dto.response.progress.LearningKitProgressResDTO;
import ch.nova_omnia.lernello.dto.response.progress.LearningUnitProgressDTO;
import ch.nova_omnia.lernello.dto.response.progress.MultipleChoiceAnswerValidationResDTO;
import ch.nova_omnia.lernello.dto.response.progress.QuestionAnswerValidationResDTO;
import ch.nova_omnia.lernello.mapper.ProgressMapper;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.scorable.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.QuestionBlock;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.model.data.progress.LearningKitProgress;
import ch.nova_omnia.lernello.model.data.progress.LearningUnitProgress;
import ch.nova_omnia.lernello.model.data.progress.block.BlockProgress;
import ch.nova_omnia.lernello.model.data.progress.block.scorable.MultipleChoiceBlockProgress;
import ch.nova_omnia.lernello.model.data.progress.block.scorable.QuestionBlockProgress;
import ch.nova_omnia.lernello.model.data.progress.block.TheoryBlockProgress;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final LearningKitProgressRepository learningKitProgressRepository;
    private final LearningUnitProgressRepository learningUnitProgressRepository;
    private final BlockProgressRepository blockProgressRepository;
    private final LearningKitRepository learningKitRepository;
    private final LearningUnitRepository learningUnitRepository;
    private final BlockRepository blockRepository;
    private final UserService userService;
    private final ProgressMapper progressMapper;

    @Transactional
    public LearningKitProgressResDTO markLearningKitOpened(LearningKitOpened dto, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        LearningKit learningKit = learningKitRepository.findById(dto.learningKitId())
            .orElseThrow(() -> new IllegalArgumentException("LearningKit not found with id: " + dto.learningKitId()));

        LearningKitProgress progress = getOrCreateLearningKitProgress(user, learningKit);
        if (!progress.isOpened()) {
            progress.setOpened(true);
        }
        progress.setLastOpenedAt(LocalDateTime.now());
        learningKitProgressRepository.save(progress);
        return progressMapper.toLearningKitProgressResDTO(progress);
    }

    @Transactional
    public LearningUnitProgressDTO markLearningUnitOpened(LearningUnitOpenedDTO dto, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        LearningUnit learningUnit = learningUnitRepository.findById(dto.learningUnitId())
            .orElseThrow(() -> new IllegalArgumentException("LearningUnit not found with id: " + dto.learningUnitId()));

        LearningKit learningKit = learningUnit.getLearningKit();
        if (learningKit == null) {
            throw new IllegalStateException("LearningUnit " + learningUnit.getUuid() + " is not associated with a LearningKit.");
        }

        LearningKitProgress kitProgress = getOrCreateLearningKitProgress(user, learningKit);
        if (!kitProgress.isOpened()) {
            kitProgress.setOpened(true);
        }
        kitProgress.setLastOpenedAt(LocalDateTime.now());

        LearningUnitProgress unitProgress = getOrCreateLearningUnitProgress(user, learningUnit, kitProgress);
        if (!unitProgress.isOpened()) {
            unitProgress.setOpened(true);
        }
        unitProgress.setLastOpenedAt(LocalDateTime.now());

        updateLearningUnitProgressPercentage(unitProgress);
        if (unitProgress.getLearningKitProgress() != null) {
            updateLearningKitProgressPercentage(unitProgress.getLearningKitProgress());
        }
        return progressMapper.toLearningUnitProgressDTO(unitProgress);
    }

    @Transactional
    public MultipleChoiceAnswerValidationResDTO checkMultipleChoiceAnswer(CheckMultipleChoiceAnswerDTO dto, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        Block block = blockRepository.findById(dto.blockId())
            .orElseThrow(() -> new IllegalArgumentException("Block not found with id: " + dto.blockId()));

        if (!(block instanceof MultipleChoiceBlock mcBlock)) {
            throw new IllegalArgumentException("Block is not a Multiple Choice Block");
        }

        LearningUnit learningUnit = mcBlock.getLearningUnit();
        if (learningUnit == null) {
            throw new IllegalStateException("Block " + mcBlock.getUuid() + " is not associated with a Learning Unit");
        }
        LearningUnitProgress unitProgress = getOrCreateLearningUnitProgress(user, learningUnit, getOrCreateLearningKitProgress(user, learningUnit.getLearningKit()));
        BlockProgress blockProgress = getOrCreateBlockProgress(user, mcBlock, unitProgress);

        boolean isCorrect = new HashSet<>(dto.answers()).equals(new HashSet<>(mcBlock.getCorrectAnswers()));

        blockProgressRepository.save(blockProgress);
        updateLearningUnitProgressPercentage(unitProgress);
        if (unitProgress.getLearningKitProgress() != null) {
            updateLearningKitProgressPercentage(unitProgress.getLearningKitProgress());
        }
        return new MultipleChoiceAnswerValidationResDTO(dto.blockId(), isCorrect);
    }

    @Transactional
    public QuestionAnswerValidationResDTO checkQuestionAnswer(CheckQuestionAnswerDTO dto, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        Block block = blockRepository.findById(dto.blockId())
            .orElseThrow(() -> new IllegalArgumentException("Block not found with id: " + dto.blockId()));

        if (!(block instanceof QuestionBlock qBlock)) {
            throw new IllegalArgumentException("Block is not a Question Block");
        }

        LearningUnit learningUnit = qBlock.getLearningUnit();
        if (learningUnit == null) {
            throw new IllegalStateException("Block " + qBlock.getUuid() + " is not associated with a Learning Unit");
        }
        LearningUnitProgress unitProgress = getOrCreateLearningUnitProgress(user, learningUnit, getOrCreateLearningKitProgress(user, learningUnit.getLearningKit()));
        BlockProgress blockProgress = getOrCreateBlockProgress(user, qBlock, unitProgress);

        boolean isCorrect = dto.answer().equalsIgnoreCase(qBlock.getExpectedAnswer());

        blockProgressRepository.save(blockProgress);
        updateLearningUnitProgressPercentage(unitProgress);
        if (unitProgress.getLearningKitProgress() != null) {
            updateLearningKitProgressPercentage(unitProgress.getLearningKitProgress());
        }
        return new QuestionAnswerValidationResDTO(dto.blockId(), isCorrect);
    }

    public LearningKitProgressResDTO getLearningKitProgress(UUID learningKitId, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        Optional<LearningKitProgress> optProgress = learningKitProgressRepository.findByUser_UuidAndLearningKit_Uuid(user.getUuid(), learningKitId);

        return optProgress
            .map(progressMapper::toLearningKitProgressResDTO)
            .orElseGet(() -> {
                LearningKit kit = learningKitRepository.findById(learningKitId)
                    .orElseThrow(() -> new IllegalArgumentException("LearningKit not found with id: " + learningKitId));
                return new LearningKitProgressResDTO(kit.getUuid(), false, false, null, null, null, 0);
            });
    }

    public LearningUnitProgressDTO getLearningUnitProgress(UUID learningUnitId, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        Optional<LearningUnitProgress> optProgress = learningUnitProgressRepository.findByUser_UuidAndLearningUnit_Uuid(user.getUuid(), learningUnitId);

        return optProgress
            .map(progressMapper::toLearningUnitProgressDTO)
            .orElseGet(() -> {
                LearningUnit unit = learningUnitRepository.findById(learningUnitId)
                    .orElseThrow(() -> new IllegalArgumentException("LearningUnit not found with id: " + learningUnitId));
                return new LearningUnitProgressDTO(unit.getUuid(), false, false, null, null, null, 0);
            });
    }

    private LearningKitProgress getOrCreateLearningKitProgress(User user, LearningKit learningKit) {
        return learningKitProgressRepository.findByUserAndLearningKit(user, learningKit)
            .orElseGet(() -> {
                LearningKitProgress newProgress = new LearningKitProgress(user, learningKit);
                return learningKitProgressRepository.save(newProgress);
            });
    }

    private LearningUnitProgress getOrCreateLearningUnitProgress(User user, LearningUnit learningUnit, LearningKitProgress kitProgress) {
        return learningUnitProgressRepository.findByUserAndLearningUnit(user, learningUnit)
            .orElseGet(() -> {
                LearningUnitProgress newProgress = new LearningUnitProgress(user, learningUnit);
                kitProgress.addLearningUnitProgress(newProgress);
                return learningUnitProgressRepository.save(newProgress);
            });
    }

    private BlockProgress getOrCreateBlockProgress(User user, Block block, LearningUnitProgress unitProgress) {
        return blockProgressRepository.findByUser_UuidAndBlock_Uuid(user.getUuid(), block.getUuid())
            .orElseGet(() -> {
                BlockProgress newBlockProgress = switch (block) {
                    case TheoryBlock theoryBlock -> new TheoryBlockProgress(user, (TheoryBlock) block, unitProgress);
                    case MultipleChoiceBlock multipleChoiceBlock -> new MultipleChoiceBlockProgress(user, (MultipleChoiceBlock) block, unitProgress);
                    case QuestionBlock questionBlock -> new QuestionBlockProgress(user, (QuestionBlock) block, unitProgress);
                    default ->
                        throw new IllegalArgumentException("Unsupported block type for progress tracking: " + block.getName());
                };
                unitProgress.addBlockProgress(newBlockProgress);
                return blockProgressRepository.save(newBlockProgress);
            });
    }

    private void updateLearningUnitProgressPercentage(LearningUnitProgress unitProgress) {
        LearningUnit learningUnit = unitProgress.getLearningUnit();
        if (learningUnit == null) return;

        List<Block> blocksInUnit = learningUnit.getBlocks();
        if (blocksInUnit.isEmpty()) {
            unitProgress.setProgressPercentage(100);
            if (!unitProgress.isCompleted()) {
                unitProgress.setCompleted(true);
                unitProgress.setCompletedAt(LocalDateTime.now());
            }
            learningUnitProgressRepository.save(unitProgress);
            return;
        }

        long completedBlocksCount = unitProgress.getUserBlockProgresses().stream()
            .filter(this::isBlockProgressConsideredComplete)
            .count();

        int percentage = (int) Math.round(((double) completedBlocksCount / blocksInUnit.size()) * 100);
        unitProgress.setProgressPercentage(percentage);

        if (percentage == 100 && !unitProgress.isCompleted()) {
            unitProgress.setCompleted(true);
            unitProgress.setCompletedAt(LocalDateTime.now());
        } else if (percentage < 100 && unitProgress.isCompleted()) {
            unitProgress.setCompleted(false);
            unitProgress.setCompletedAt(null);
        }
        learningUnitProgressRepository.save(unitProgress);
    }

    private void updateLearningKitProgressPercentage(LearningKitProgress kitProgress) {
        LearningKit learningKit = kitProgress.getLearningKit();
        if (learningKit == null) return;

        List<LearningUnit> learningUnitsInKit = learningKit.getLearningUnits();
        if (learningUnitsInKit.isEmpty()) {
            kitProgress.setProgressPercentage(100);
            if (!kitProgress.isCompleted()) {
                kitProgress.setCompleted(true);
                kitProgress.setCompletedAt(LocalDateTime.now());
            }
            learningKitProgressRepository.save(kitProgress);
            return;
        }

        long totalBlocksInKit = 0;
        long totalCompletedBlocksInKit = 0;

        User user = kitProgress.getUser();

        for (LearningUnit lu : learningUnitsInKit) {
            totalBlocksInKit += lu.getBlocks().size();
            LearningUnitProgress lup = getOrCreateLearningUnitProgress(user, lu, kitProgress);
            totalCompletedBlocksInKit += lup.getUserBlockProgresses().stream()
                .filter(this::isBlockProgressConsideredComplete)
                .count();
        }

        if (totalBlocksInKit == 0) {
            kitProgress.setProgressPercentage(100);
        } else {
            int percentage = (int) Math.round(((double) totalCompletedBlocksInKit / totalBlocksInKit) * 100);
            kitProgress.setProgressPercentage(percentage);
        }

        if (kitProgress.getProgressPercentage() == 100 && !kitProgress.isCompleted()) {
            kitProgress.setCompleted(true);
            kitProgress.setCompletedAt(LocalDateTime.now());
        } else if (kitProgress.getProgressPercentage() < 100 && kitProgress.isCompleted()) {
            kitProgress.setCompleted(false);
            kitProgress.setCompletedAt(null);
        }
        learningKitProgressRepository.save(kitProgress);
    }

    private boolean isBlockProgressConsideredComplete(BlockProgress bp) {
        Block actualBlockEntity = bp.getBlock();

        if (bp instanceof MultipleChoiceBlockProgress mcProgress && actualBlockEntity instanceof MultipleChoiceBlock mcBlockDefinition) {
            List<String> lastAnswers = mcProgress.getLastAnswers();
            List<String> correctAnswers = mcBlockDefinition.getCorrectAnswers();

            return correctAnswers != null && !correctAnswers.isEmpty() && lastAnswers != null && new HashSet<>(lastAnswers).equals(new HashSet<>(correctAnswers));
        } else if (bp instanceof QuestionBlockProgress qProgress && actualBlockEntity instanceof QuestionBlock qBlockDefinition) {
            String lastAnswer = qProgress.getLastAnswer();
            String expectedAnswer = qBlockDefinition.getExpectedAnswer();

            return lastAnswer != null && expectedAnswer != null && lastAnswer.equalsIgnoreCase(expectedAnswer);
        } else if (bp instanceof TheoryBlockProgress) {
            return true;
        }
        return false;
    }
}
