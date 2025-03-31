package ch.nova_omnia.lernello.dto.request;

import ch.nova_omnia.lernello.dto.response.user.LoggedInUserDTO;
import ch.nova_omnia.lernello.model.data.File;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record CreateLearningKitDTO(
        @NotNull @Size(min = 3, max = 40) String name,
        String description,
        @NotNull String language,
        Date deadlineDate,
        List<LoggedInUserDTO> participants,
        List<File> files,
        UUID folderId
    ){
}
