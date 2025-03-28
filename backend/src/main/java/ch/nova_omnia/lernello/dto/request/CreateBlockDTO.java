package ch.nova_omnia.lernello.dto.request;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateBlockDTO {
    private String blockType;
    private String name;
    private int position;
    private UUID learningUnitId;
}