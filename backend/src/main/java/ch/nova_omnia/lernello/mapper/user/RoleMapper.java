package ch.nova_omnia.lernello.mapper.user;

import ch.nova_omnia.lernello.dto.response.user.UserRoleDTO;
import ch.nova_omnia.lernello.model.data.user.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    UserRoleDTO toDTO(Role role);
}
