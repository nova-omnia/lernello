package ch.nova_omnia.lernello.dto.response.block;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionBlockResDTO extends BlockResDTO {

    @NotBlank
    private String question;
    @NotBlank
    private String expectedAnswer;

    public QuestionBlockResDTO(UUID uuid, String name, int position, String question, String expectedAnswer) {
        super(uuid, name, position);
        this.question = question;
        this.expectedAnswer = expectedAnswer;
    }
}
