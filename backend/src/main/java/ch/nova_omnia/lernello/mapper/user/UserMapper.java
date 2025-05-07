package ch.nova_omnia.lernello.mapper.user;

import ch.nova_omnia.lernello.dto.request.user.UpdateUserDTO;
import ch.nova_omnia.lernello.dto.response.user.UserResDTO;
import ch.nova_omnia.lernello.model.data.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResDTO toDTO(User user);

    User toEntity(UpdateUserDTO userDTO);
}
