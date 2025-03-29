package ch.nova_omnia.lernello.dto.request.block;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CreateMultipleChoiceBlockDTO extends CreateBlockDTO {
    private String question;
    private List<String> possibleAnswers;
    private List<String> correctAnswers;
}
