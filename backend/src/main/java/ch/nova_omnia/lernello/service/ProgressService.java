package ch.nova_omnia.lernello.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.dto.request.progress.CheckMultipleChoiceAnswerDTO;
import ch.nova_omnia.lernello.dto.request.progress.CheckQuestionAnswerDTO;
import ch.nova_omnia.lernello.dto.request.progress.LearningKitOpened;
import ch.nova_omnia.lernello.dto.request.progress.LearningUnitOpenedDTO;
import ch.nova_omnia.lernello.dto.request.progress.TheoryBlockViewedDTO;
import ch.nova_omnia.lernello.dto.response.progress.MultipleChoiceAnswerValidationResDTO;
import ch.nova_omnia.lernello.dto.response.progress.QuestionAnswerValidationResDTO;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.BlockType;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.model.data.block.TranslatedBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.QuestionBlock;
import ch.nova_omnia.lernello.model.data.progress.LearningKitProgress;
import ch.nova_omnia.lernello.model.data.progress.LearningUnitProgress;
import ch.nova_omnia.lernello.model.data.progress.block.BlockProgress;
import ch.nova_omnia.lernello.model.data.progress.block.TheoryBlockProgress;
import ch.nova_omnia.lernello.model.data.progress.block.scorable.MultipleChoiceBlockProgress;
import ch.nova_omnia.lernello.model.data.progress.block.scorable.QuestionBlockProgress;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.BlockProgressRepository;
import ch.nova_omnia.lernello.repository.BlockRepository;
import ch.nova_omnia.lernello.repository.LearningKitProgressRepository;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.LearningUnitProgressRepository;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import ch.nova_omnia.lernello.service.block.AIBlockService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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
    private final AIBlockService aiBlockService;

    @Transactional
    public LearningKitProgress markLearningKitOpened(LearningKitOpened dto, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        updateLearningKitProgressPercentage(
            getOrCreateLearningKitProgress(user, learningKitRepository.findById(dto.learningKitId())
                .orElseThrow(() -> new IllegalArgumentException("LearningKit not found with id: " + dto.learningKitId()))));
        LearningKit learningKit = learningKitRepository.findById(dto.learningKitId()).orElseThrow(() -> new IllegalArgumentException("LearningKit not found with id: " + dto.learningKitId()));

        LearningKitProgress progress = getOrCreateLearningKitProgress(user, learningKit);
        if (!progress.isOpened()) {
            progress.setOpened(true);
        }
        progress.setLastOpenedAt(ZonedDateTime.now());
        learningKitProgressRepository.save(progress);
        return progress;
    }

    @Transactional
    public LearningUnitProgress markLearningUnitOpened(LearningUnitOpenedDTO dto, UserDetails userDetails) {
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
        kitProgress.setLastOpenedAt(ZonedDateTime.now());

        LearningUnitProgress unitProgress = getOrCreateLearningUnitProgress(user, learningUnit, kitProgress);
        if (!unitProgress.isOpened()) {
            unitProgress.setOpened(true);
        }
        unitProgress.setLastOpenedAt(ZonedDateTime.now());

        updateLearningUnitProgressPercentage(unitProgress);
        updateLearningKitProgressPercentage(kitProgress);

        return unitProgress;
    }

    @Transactional
    public MultipleChoiceAnswerValidationResDTO checkMultipleChoiceAnswer(CheckMultipleChoiceAnswerDTO dto, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        Block block = blockRepository.findById(dto.blockId()).orElseThrow(() -> new IllegalArgumentException("Block not found with id: " + dto.blockId()));

        MultipleChoiceBlock mcBlock;
        if (!(block instanceof MultipleChoiceBlock)) {
            if (block instanceof TranslatedBlock translatedBlock && block.getType().equals(BlockType.MULTIPLE_CHOICE)) {
                mcBlock = (MultipleChoiceBlock) translatedBlock.getOriginalBlock();
            } else {
                throw new IllegalArgumentException("Block is not a Multiple Choice Block");
            }
        } else {
            mcBlock = (MultipleChoiceBlock) block;
        }

        LearningUnit learningUnit = mcBlock.getLearningUnit();
        if (learningUnit == null) {
            throw new IllegalStateException("Block " + mcBlock.getUuid() + " is not associated with a Learning Unit");
        }
        LearningUnitProgress unitProgress = getOrCreateLearningUnitProgress(user, learningUnit, getOrCreateLearningKitProgress(user, learningUnit.getLearningKit()));
        BlockProgress blockProgress = getOrCreateBlockProgress(user, mcBlock, unitProgress);

        if (blockProgress instanceof MultipleChoiceBlockProgress mcProgress) {
            mcProgress.setLastAnswers(new ArrayList<>(dto.answers()));
        }

        boolean isCorrect = new HashSet<>(dto.answers()).equals(new HashSet<>(mcBlock.getCorrectAnswers()));

        if (blockProgress instanceof MultipleChoiceBlockProgress mcProgress) {
            mcProgress.setIsCorrect(isCorrect);
            mcProgress.setLastAnswers(new ArrayList<>(dto.answers()));
        }

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
        Block block = blockRepository.findById(dto.blockId()).orElseThrow(() -> new IllegalArgumentException("Block not found with id: " + dto.blockId()));

        QuestionBlock qBlock;
        if (!(block instanceof QuestionBlock)) {
            if (block instanceof TranslatedBlock translatedBlock && block.getType().equals(BlockType.QUESTION)) {
                qBlock = (QuestionBlock) translatedBlock.getOriginalBlock();
            } else {
                throw new IllegalArgumentException("Block is not a Question Block");
            }
        } else {
            qBlock = (QuestionBlock) block;
        }

        LearningUnit learningUnit = qBlock.getLearningUnit();
        if (learningUnit == null) {
            throw new IllegalStateException("Block " + qBlock.getUuid() + " is not associated with a Learning Unit");
        }
        LearningUnitProgress unitProgress = getOrCreateLearningUnitProgress(user, learningUnit, getOrCreateLearningKitProgress(user, learningUnit.getLearningKit()));
        BlockProgress blockProgress = getOrCreateBlockProgress(user, qBlock, unitProgress);

        if (blockProgress instanceof QuestionBlockProgress mcProgress) {
            mcProgress.setLastAnswer(dto.answer());
        }

        boolean isCorrect = aiBlockService.checkQuestionAnswerWithAI(dto.answer(), qBlock.getExpectedAnswer());

        if (blockProgress instanceof QuestionBlockProgress qProgress) {
            qProgress.setIsCorrect(isCorrect);
            qProgress.setLastAnswer(dto.answer());
        }

        blockProgressRepository.save(blockProgress);
        updateLearningUnitProgressPercentage(unitProgress);
        if (unitProgress.getLearningKitProgress() != null) {
            updateLearningKitProgressPercentage(unitProgress.getLearningKitProgress());
        }
        return new QuestionAnswerValidationResDTO(dto.blockId(), isCorrect);
    }

    @Transactional
    public TheoryBlockProgress markTheoryBlockViewed(TheoryBlockViewedDTO dto, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        Block block = blockRepository.findById(dto.blockId()).orElseThrow(() -> new IllegalArgumentException("Block not found with id: " + dto.blockId()));

        TheoryBlock theoryBlock;
        if (!(block instanceof TheoryBlock)) {
            if (block instanceof TranslatedBlock translatedBlock && block.getType().equals(BlockType.THEORY)) {
                theoryBlock = (TheoryBlock) translatedBlock.getOriginalBlock();
            } else {
                throw new IllegalArgumentException("Block is not a Theory Block");
            }
        } else {
            theoryBlock = (TheoryBlock) block;
        }

        LearningUnit learningUnit = theoryBlock.getLearningUnit();
        if (learningUnit == null) {
            throw new IllegalStateException("Block " + theoryBlock.getUuid() + " is not associated with a Learning Unit");
        }

        LearningKit learningKit = learningUnit.getLearningKit();
        if (learningKit == null) {
            throw new IllegalStateException("LearningUnit " + learningUnit.getUuid() + " is not associated with a LearningKit.");
        }

        LearningKitProgress kitProgress = getOrCreateLearningKitProgress(user, learningKit);
        LearningUnitProgress unitProgress = getOrCreateLearningUnitProgress(user, learningUnit, kitProgress);

        BlockProgress blockProgress = getOrCreateBlockProgress(user, theoryBlock, unitProgress);

        if (blockProgress instanceof TheoryBlockProgress theoryBlockProgress) {
            theoryBlockProgress.setIsViewed(true);
        }

        blockProgressRepository.save(blockProgress);

        updateLearningUnitProgressPercentage(unitProgress);
        if (unitProgress.getLearningKitProgress() != null) {
            updateLearningKitProgressPercentage(unitProgress.getLearningKitProgress());
        }

        if (blockProgress instanceof TheoryBlockProgress) {
            return (TheoryBlockProgress) blockProgress;
        }
        throw new IllegalStateException("BlockProgress is not of type TheoryBlockProgress");
    }

    public LearningKitProgress getLearningKitProgress(UUID learningKitId, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        Optional<LearningKitProgress> optProgress = learningKitProgressRepository.findByUser_UuidAndLearningKit_Uuid(user.getUuid(), learningKitId);

        return optProgress.orElseGet(() -> {
            LearningKit kit = learningKitRepository.findById(learningKitId).orElseThrow(() -> new IllegalArgumentException("LearningKit not found with id: " + learningKitId));
            return new LearningKitProgress(user, kit);
        });
    }

    @Transactional
    public List<LearningKitProgress> getLearningKitProgressForAllTrainees(UUID learningKitId) {
        LearningKit learningKit = learningKitRepository.findById(learningKitId).orElseThrow(() -> new IllegalArgumentException("LearningKit not found with id: " + learningKitId));

        List<User> trainees = learningKit.getTrainees();
        List<LearningKitProgress> allProgresses = new ArrayList<>();

        for (User trainee : trainees) {
            LearningKitProgress progress = getOrCreateLearningKitProgress(trainee, learningKit);
            allProgresses.add(progress);
        }
        return allProgresses;
    }

    public LearningUnitProgress getLearningUnitProgress(UUID learningUnitId, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        Optional<LearningUnitProgress> optProgress = learningUnitProgressRepository.findByUser_UuidAndLearningUnit_Uuid(user.getUuid(), learningUnitId);

        return optProgress.orElseGet(() -> {
            LearningUnit unit = learningUnitRepository.findById(learningUnitId).orElseThrow(() -> new IllegalArgumentException("LearningUnit not found with id: " + learningUnitId));
            return new LearningUnitProgress(user, unit);
        });
    }

    private LearningKitProgress getOrCreateLearningKitProgress(User user, LearningKit learningKit) {
        return learningKitProgressRepository.findByUser_UuidAndLearningKit_Uuid(user.getUuid(), learningKit.getUuid()).orElseGet(() -> {
            LearningKitProgress newKitProgress = new LearningKitProgress(user, learningKit);
            LearningKitProgress savedKitProgress = learningKitProgressRepository.save(newKitProgress);

            for (LearningUnit learningUnit : learningKit.getLearningUnits()) {
                getOrCreateLearningUnitProgress(user, learningUnit, savedKitProgress);
            }
            return savedKitProgress;
        });
    }

    private LearningUnitProgress getOrCreateLearningUnitProgress(User user, LearningUnit learningUnit, LearningKitProgress kitProgress) {
        return learningUnitProgressRepository.findByUser_UuidAndLearningUnit_Uuid(user.getUuid(), learningUnit.getUuid()).orElseGet(() -> {
            LearningUnitProgress newProgress = new LearningUnitProgress(user, learningUnit);

            if (kitProgress != null) {
                newProgress.setLearningKitProgress(kitProgress);
            }

            LearningUnitProgress savedUnitProgress = learningUnitProgressRepository.save(newProgress);

            for (Block block : learningUnit.getBlocks()) {
                getOrCreateBlockProgress(user, block, savedUnitProgress);
            }
            if (kitProgress != null) {
                kitProgress.addLearningUnitProgress(savedUnitProgress);
            }
            return savedUnitProgress;
        });
    }

    private BlockProgress getOrCreateBlockProgress(User user, Block block, LearningUnitProgress unitProgress) {
        return blockProgressRepository.findByUser_UuidAndBlock_Uuid(user.getUuid(), block.getUuid()).orElseGet(() -> {
            BlockProgress newBlockProgress = switch (block) {
                case TheoryBlock _ -> new TheoryBlockProgress(user, (TheoryBlock) block, unitProgress);
                case MultipleChoiceBlock _ -> new MultipleChoiceBlockProgress(user, (MultipleChoiceBlock) block, unitProgress);
                case QuestionBlock _ -> new QuestionBlockProgress(user, (QuestionBlock) block, unitProgress);
                default -> throw new IllegalArgumentException("Unsupported block type for progress tracking: " + block.getName());
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
                unitProgress.setCompletedAt(ZonedDateTime.now());
            }
            learningUnitProgressRepository.save(unitProgress);
            return;
        }

        List<BlockProgress> currentBlockProgresses = blockProgressRepository.findByLearningUnitProgress_Uuid(unitProgress.getUuid());

        long completedBlocksCount = currentBlockProgresses.stream().filter(this::isBlockProgressConsideredComplete).count();

        int percentage = (int) Math.round(((double) completedBlocksCount / blocksInUnit.size()) * 100);
        unitProgress.setProgressPercentage(percentage);

        if (percentage == 100 && !unitProgress.isCompleted()) {
            unitProgress.setCompleted(true);
            unitProgress.setCompletedAt(ZonedDateTime.now());
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
                kitProgress.setCompletedAt(ZonedDateTime.now());
            }
            learningKitProgressRepository.save(kitProgress);
            return;
        }

        long totalBlocksInKit = 0;
        long totalCompletedBlocksInKit = 0;

        User user = kitProgress.getUser();

        for (LearningUnit learningUnit : learningUnitsInKit) {
            totalBlocksInKit += learningUnit.getBlocks().size();
            LearningUnitProgress learningUnitProgress = getOrCreateLearningUnitProgress(user, learningUnit, kitProgress);

            List<BlockProgress> blockProgressesInLup = blockProgressRepository.findByLearningUnitProgress_Uuid(learningUnitProgress.getUuid());

            totalCompletedBlocksInKit += blockProgressesInLup.stream().filter(this::isBlockProgressConsideredComplete).count();
        }

        if (totalBlocksInKit == 0) {
            kitProgress.setProgressPercentage(100);
        } else {
            int percentage = (int) Math.round(((double) totalCompletedBlocksInKit / totalBlocksInKit) * 100);
            kitProgress.setProgressPercentage(percentage);
        }

        if (kitProgress.getProgressPercentage() == 100 && !kitProgress.isCompleted()) {
            kitProgress.setCompleted(true);
            kitProgress.setCompletedAt(ZonedDateTime.now());
        } else if (kitProgress.getProgressPercentage() < 100 && kitProgress.isCompleted()) {
            kitProgress.setCompleted(false);
            kitProgress.setCompletedAt(null);
        }
        learningKitProgressRepository.save(kitProgress);
    }

    private boolean isBlockProgressConsideredComplete(BlockProgress blockProgress) {
        Block actualBlockEntity = blockProgress.getBlock();

        switch (blockProgress) {
            case MultipleChoiceBlockProgress mcProgress when actualBlockEntity instanceof MultipleChoiceBlock mcBlockDefinition -> {
                List<String> lastAnswers = mcProgress.getLastAnswers();
                List<String> correctAnswers = mcBlockDefinition.getCorrectAnswers();

                return correctAnswers != null && !correctAnswers.isEmpty() && lastAnswers != null && new HashSet<>(lastAnswers).equals(new HashSet<>(correctAnswers));
            }
            case QuestionBlockProgress qProgress when actualBlockEntity instanceof QuestionBlock qBlockDefinition -> {
                String lastAnswer = qProgress.getLastAnswer();
                String expectedAnswer = qBlockDefinition.getExpectedAnswer();

                return lastAnswer != null && expectedAnswer != null && lastAnswer.equalsIgnoreCase(expectedAnswer);
            }
            case TheoryBlockProgress theoryBlockProgress -> {
                return theoryBlockProgress.getIsViewed();
            }
            default -> {
            }
        }
        return false;
    }
}
