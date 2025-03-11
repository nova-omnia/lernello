package ch.nova_omnia.pm4.mapper;

import ch.nova_omnia.pm4.dto.InstructorDto;
import ch.nova_omnia.pm4.model.data.Instructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {FolderMapper.class})
public interface InstructorMapper extends GenericMapper<Instructor, InstructorDto> {
    InstructorMapper INSTANCE = Mappers.getMapper(InstructorMapper.class);

    InstructorDto toDto(Instructor instructor);
}
