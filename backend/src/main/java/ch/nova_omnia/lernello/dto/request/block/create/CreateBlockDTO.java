package ch.nova_omnia.lernello.dto.request.block.create;

public sealed interface CreateBlockDTO permits
        CreateMultipleChoiceBlockDTO,
        CreateQuestionBlockDTO,
        CreateTheoryBlockDTO
{
}
