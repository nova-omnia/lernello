package ch.nova_omnia.lernello.mapper.user;

import ch.nova_omnia.lernello.dto.response.user.ParticipantUserDTO;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ParticipantUserMapper {
    ParticipantUserDTO toDTO(UUID uuid, String username);
}
