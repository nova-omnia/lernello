package ch.nova_omnia.lernello.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.List;

public record CreateLerningKitDTO(
    @NotNull @Size(min = 2, max = 40) String name,
    String description,
    @NotNull String language,
    Date dealineDate,
    Date startDate,
    Date endDate,
    List<String> participants //TODO: what datatype and maybe with ids of participants?
    ){
}
