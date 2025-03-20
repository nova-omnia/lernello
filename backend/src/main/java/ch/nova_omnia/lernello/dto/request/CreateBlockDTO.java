package ch.nova_omnia.lernello.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateBlockDTO(
        @NotNull
        @Pattern(regexp = "^(quiz|theory|multimedia)$", message = "Type must be either 'quiz', 'theory' or 'multimedia'")
        String type,
        @NotNull
        @Size(min = 3, max = 40, message = "the name does not fulfill the size requirements")
        String name) {
}