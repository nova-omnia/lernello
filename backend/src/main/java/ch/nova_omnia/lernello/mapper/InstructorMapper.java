package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ch.nova_omnia.lernello.dto.response.InstructorResDTO;
import ch.nova_omnia.lernello.model.data.Instructor;

@Mapper(componentModel = "spring")
public interface InstructorMapper {
    InstructorMapper INSTANCE = Mappers.getMapper(InstructorMapper.class);

    InstructorResDTO toDTO(Instructor instructor);
}
