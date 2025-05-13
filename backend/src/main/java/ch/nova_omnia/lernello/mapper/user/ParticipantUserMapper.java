package ch.nova_omnia.lernello.mapper.user;

import ch.nova_omnia.lernello.dto.response.user.ParticipantUserDTO;
import ch.nova_omnia.lernello.model.data.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParticipantUserMapper {
    ParticipantUserDTO toDTO(User user);
}
