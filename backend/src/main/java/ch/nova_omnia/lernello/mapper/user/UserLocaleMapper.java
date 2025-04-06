package ch.nova_omnia.lernello.mapper.user;

import org.mapstruct.Mapper;

import ch.nova_omnia.lernello.dto.request.user.UserLocaleDTO;

@Mapper(componentModel = "spring")
public interface UserLocaleMapper {
    UserLocaleDTO toDTO(String locale);
}
