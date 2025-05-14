package ch.nova_omnia.lernello.dto.response.progress;

import ch.nova_omnia.lernello.model.data.block.BlockType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "blockType"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = TheoryBlockProgressResDTO.class, name = "THEORY"),
    @JsonSubTypes.Type(value = MultipleChoiceBlockProgressResDTO.class, name = "MULTIPLE_CHOICE"),
    @JsonSubTypes.Type(value = QuestionBlockProgressResDTO.class, name = "QUESTION")
})
public sealed interface BlockProgressResDTO permits TheoryBlockProgressResDTO, MultipleChoiceBlockProgressResDTO, QuestionBlockProgressResDTO {
    UUID blockId();
    BlockType blockType();
}
