package ch.nova_omnia.lernello.dto.response.block;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MultipleChoiceBlockResDTO extends BlockResDTO {
    
    @NotBlank
    private String question;
    @NotBlank
    private List<String> possibleAnswers= new ArrayList<>();
    @NotBlank
    private List<String> correctAnswers = new ArrayList<>();

    public MultipleChoiceBlockResDTO(UUID uuid, String name, int position, String question, List<String> possibleAnswers, List<String> correctAnswers) {
        super(uuid, name, position);
        this.question = question;
        this.possibleAnswers = possibleAnswers;
        this.correctAnswers = correctAnswers;
    }

}
