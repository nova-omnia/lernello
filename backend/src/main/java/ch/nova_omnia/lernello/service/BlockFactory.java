package ch.nova_omnia.lernello.service;

import org.springframework.stereotype.Component;

import ch.nova_omnia.lernello.dto.request.CreateBlockDTO;
import ch.nova_omnia.lernello.dto.request.CreateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.dto.request.CreateQuestionBlockDTO;
import ch.nova_omnia.lernello.dto.request.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.model.data.blocks.Block;
import ch.nova_omnia.lernello.model.data.blocks.MultipleChoiceBlock;
import ch.nova_omnia.lernello.model.data.blocks.QuestionBlock;
import ch.nova_omnia.lernello.model.data.blocks.TheoryBlock;

@Component
public class BlockFactory {

    public Block createBlock(CreateBlockDTO dto) {
        switch (dto.getBlockType()) {
            case "THEORY":
                if (dto instanceof CreateTheoryBlockDTO) {
                    CreateTheoryBlockDTO theoryDto = (CreateTheoryBlockDTO) dto;
                    return new TheoryBlock(
                            theoryDto.getName(), theoryDto.getPosition(), dto.getBlockType(), null, theoryDto.getContent()
                    );
                }
                break;
            case "MULTIPLE_CHOICE":
                if (dto instanceof CreateMultipleChoiceBlockDTO) {
                    CreateMultipleChoiceBlockDTO mcDto = (CreateMultipleChoiceBlockDTO) dto;
                    return new MultipleChoiceBlock(
                            mcDto.getName(), mcDto.getPosition(), dto.getBlockType(), null, mcDto.getQuestion(), mcDto.getPossibleAnswers(), mcDto.getCorrectAnswers()
                    );
                }
                break;
            case "QUESTION":
                if (dto instanceof CreateQuestionBlockDTO) {
                    CreateQuestionBlockDTO questionDto = (CreateQuestionBlockDTO) dto;
                    return new QuestionBlock(
                            questionDto.getName(), questionDto.getPosition(), dto.getBlockType(), null, questionDto.getQuestion(), questionDto.getExpectedAnswer()
                    );
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported block type: " + dto.getBlockType());
        }
        throw new IllegalArgumentException("Invalid DTO for block type: " + dto.getBlockType());
    }
}