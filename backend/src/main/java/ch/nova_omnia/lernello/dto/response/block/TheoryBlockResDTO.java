package ch.nova_omnia.lernello.dto.response.block;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TheoryBlockResDTO extends BlockResDTO {

    @NotBlank
    private String content;
    
    public TheoryBlockResDTO(UUID uuid, String name, int position, String content) {
        super(uuid, name, position);
        this.content = content;
    }
}