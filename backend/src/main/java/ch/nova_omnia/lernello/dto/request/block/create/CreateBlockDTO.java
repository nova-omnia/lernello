package ch.nova_omnia.lernello.dto.request.block.create;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateMultipleChoiceBlockDTO.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = CreateQuestionBlockDTO.class, name = "QUESTION"),
        @JsonSubTypes.Type(value = CreateTheoryBlockDTO.class, name = "THEORY")
})
public sealed interface CreateBlockDTO
        permits CreateMultipleChoiceBlockDTO, CreateQuestionBlockDTO, CreateTheoryBlockDTO {
}