package ch.nova_omnia.lernello.dto.request.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CreateTheoryBlockDTO extends CreateBlockDTO {
    private String content;
    
}