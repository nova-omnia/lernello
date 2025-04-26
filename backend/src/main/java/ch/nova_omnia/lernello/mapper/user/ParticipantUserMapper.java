package ch.nova_omnia.lernello.mapper.user;

import org.mapstruct.Mapper;

import ch.nova_omnia.lernello.dto.response.user.ParticipantUserDTO;
import ch.nova_omnia.lernello.model.data.user.User;

@Mapper(componentModel = "spring")
public interface ParticipantUserMapper {
    ParticipantUserDTO toDTO(User user);
}
