package ch.nova_omnia.lernello.mapper.user;

import ch.nova_omnia.lernello.dto.request.user.UserLocaleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserLocaleMapper {
    UserLocaleDTO toDTO(String locale);
}
