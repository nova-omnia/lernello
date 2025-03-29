package ch.nova_omnia.lernello.dto.response.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class TheoryBlockResDTO extends BlockResDTO {
    private String content;
}