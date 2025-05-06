package ch.nova_omnia.lernello.dto.request.block.blockActions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AddBlockActionDTO.class, name = "ADD_BLOCK"),
        @JsonSubTypes.Type(value = ReorderBlockActionDTO.class, name = "REORDER_BLOCK"),
        @JsonSubTypes.Type(value = RemoveBlockActionDTO.class, name = "REMOVE_BLOCK"),
        @JsonSubTypes.Type(value = UpdateBlockActionDTO.class, name = "UPDATE_BLOCK"),
        @JsonSubTypes.Type(value = UpdateBlockNameActionDTO.class, name = "UPDATE_BLOCK_NAME")
})
public sealed interface BlockActionDTO permits AddBlockActionDTO, ReorderBlockActionDTO, RemoveBlockActionDTO, UpdateBlockActionDTO, UpdateBlockNameActionDTO {
}
