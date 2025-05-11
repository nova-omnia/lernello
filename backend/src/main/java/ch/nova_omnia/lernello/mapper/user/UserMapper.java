package ch.nova_omnia.lernello.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import ch.nova_omnia.lernello.dto.request.user.UpdateUserDTO;
import ch.nova_omnia.lernello.dto.response.user.UserResDTO;
import ch.nova_omnia.lernello.model.data.user.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResDTO toDTO(User user);

    User toEntity(UpdateUserDTO userDTO);
}
