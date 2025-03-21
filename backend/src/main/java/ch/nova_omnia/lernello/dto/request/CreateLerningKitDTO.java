package ch.nova_omnia.lernello.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateLerningKitDTO(
    @NotNull @Size(min = 2, max = 40) String name
    ){
}
