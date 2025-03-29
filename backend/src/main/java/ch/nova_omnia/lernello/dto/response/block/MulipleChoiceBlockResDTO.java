package ch.nova_omnia.lernello.dto.response.block;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class MulipleChoiceBlockResDTO {
    private String question;
    private List<String> possibleAnswers;
    private List<String> correctAnswers;
}
