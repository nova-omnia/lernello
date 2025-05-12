package ch.nova_omnia.lernello.mapper;

import static ch.nova_omnia.lernello.model.data.block.BlockType.MULTIPLE_CHOICE;
import static ch.nova_omnia.lernello.model.data.block.BlockType.QUESTION;
import static ch.nova_omnia.lernello.model.data.block.BlockType.THEORY;

import ch.nova_omnia.lernello.dto.request.CreateLearningUnitDTO;
import ch.nova_omnia.lernello.dto.response.LearningUnitResDTO;
import ch.nova_omnia.lernello.dto.response.block.*;
import ch.nova_omnia.lernello.model.data.block.*;
import ch.nova_omnia.lernello.model.data.block.scorable.*;
import ch.nova_omnia.lernello.repository.TranslatedBlockRepository;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LearningUnitMapper {
    private final TranslatedBlockRepository translatedBlockRepository;

    public LearningUnit toEntity(CreateLearningUnitDTO dto, LearningKit learningKit) {
        return new LearningUnit(dto.name(), learningKit);
    }

    public LearningUnitResDTO toDTO(LearningUnit learningUnit) {
        List<BlockResDTO> sortedBlocks = learningUnit.getBlocks().stream().sorted(Comparator.comparingInt(Block::getPosition)).map(this::mapBlockToResDTO).toList();

        return new LearningUnitResDTO(
                learningUnit.getUuid(), learningUnit.getName(), sortedBlocks, learningUnit.getPosition()
        );
    }

    public BlockResDTO mapBlockToResDTO(Block block) {
        List<TranslatedBlockResDTO> translations = translatedBlockRepository.findByOriginalBlock(block).stream().map(this::toTranslatedDto).toList();

        if (block instanceof MultipleChoiceBlock mc) {
            return new MultipleChoiceBlockResDTO(
                    MULTIPLE_CHOICE, mc.getUuid(), mc.getName(), mc.getPosition(), mc.getQuestion(), mc.getPossibleAnswers(), mc.getCorrectAnswers(), translations
            );
        } else if (block instanceof QuestionBlock q) {
            return new QuestionBlockResDTO(
                    QUESTION, q.getUuid(), q.getName(), q.getPosition(), q.getQuestion(), q.getExpectedAnswer(), translations
            );
        } else if (block instanceof TheoryBlock t) {
            return new TheoryBlockResDTO(
                    THEORY, t.getUuid(), t.getName(), t.getPosition(), t.getContent(), translations
            );
        }

        throw new IllegalArgumentException("Unknown block type: " + block.getClass().getName());
    }

    public TranslatedBlockResDTO toTranslatedDto(TranslatedBlock b) {
        return new TranslatedBlockResDTO(
                b.getUuid(), b.getLanguage(), b.getName(), b.getContent(), b.getQuestion(), b.getExpectedAnswer(), b.getPossibleAnswers(), b.getCorrectAnswers()
        );
    }

    public List<TranslatedBlockResDTO> mapTranslatedBlocks(List<TranslatedBlock> blocks) {
        return blocks.stream().map(this::toTranslatedDto).toList();
    }
}
