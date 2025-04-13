package ch.nova_omnia.lernello.mapper.user;

import org.mapstruct.Mapper;

import ch.nova_omnia.lernello.dto.response.user.UserInfoDTO;
import ch.nova_omnia.lernello.model.data.User;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {
    UserInfoDTO toDTO(User user);
}
