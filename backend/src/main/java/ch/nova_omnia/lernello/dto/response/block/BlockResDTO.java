package ch.nova_omnia.lernello.dto.response.block;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;


public sealed interface BlockResDTO permits MultipleChoiceBlockResDTO, QuestionBlockResDTO, TheoryBlockResDTO {
}
