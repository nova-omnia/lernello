package ch.nova_omnia.lernello.dto.request;

import ch.nova_omnia.lernello.dto.response.user.LoggedInUserDTO;
import ch.nova_omnia.lernello.dto.response.user.ParticipantUserDTO;
import ch.nova_omnia.lernello.model.data.File;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record CreateLearningKitDTO(
        UUID uuid,
        @NotNull @Size(min = 3, max = 40) String name,
        String description,
        @NotNull String language,
        LocalDate deadlineDate,
        List<ParticipantUserDTO> participants,
        UUID folderId,
        String context
        //MultipartFile[] files, ToDo
    ){
}
