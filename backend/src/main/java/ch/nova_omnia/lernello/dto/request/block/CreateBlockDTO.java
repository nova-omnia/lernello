package ch.nova_omnia.lernello.dto.request.block;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class CreateBlockDTO {
    private String blockType;
    private String name;
    private int position;
    private UUID learningUnitId;
}