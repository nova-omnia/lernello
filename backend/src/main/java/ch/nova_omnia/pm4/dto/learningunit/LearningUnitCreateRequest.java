package ch.nova_omnia.pm4.dto.learningunit;

import jakarta.validation.constraints.NotBlank;

public class LearningUnitCreateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
