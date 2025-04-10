package ch.nova_omnia.lernello.dto.response.block;

public sealed interface BlockResDTO permits MultipleChoiceBlockResDTO, QuestionBlockResDTO, TheoryBlockResDTO {
}
