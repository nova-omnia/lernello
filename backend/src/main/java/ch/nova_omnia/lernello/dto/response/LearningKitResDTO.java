package ch.nova_omnia.lernello.dto.response;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.dto.response.user.LoggedInUserDTO;
import ch.nova_omnia.lernello.model.data.File;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LearningKitResDTO(
        @NotNull UUID uuid,
        @NotNull @Size(min = 3, max = 40) String name,
        List<LearningUnitResDTO> learningUnits,
        String description,
        @NotNull String language,
        LocalDate deadlineDate,
        List<LoggedInUserDTO> participants,
        UUID folderId
        //List<File> files, ToDo
) {
}
