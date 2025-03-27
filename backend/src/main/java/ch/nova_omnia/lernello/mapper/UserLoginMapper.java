package ch.nova_omnia.lernello.mapper;

import ch.nova_omnia.lernello.dto.response.user.LoggedInUserDTO;
import ch.nova_omnia.lernello.model.data.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserLoginMapper {
    LoggedInUserDTO toDTO(User user);
}
