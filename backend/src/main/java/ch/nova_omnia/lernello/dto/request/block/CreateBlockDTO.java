package ch.nova_omnia.lernello.dto.request.block;

import java.util.UUID;

import ch.nova_omnia.lernello.utilities.BlockType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class CreateBlockDTO {
    private BlockType blockType;
    private String name;
    private int position;
    private UUID learningUnitId;
}