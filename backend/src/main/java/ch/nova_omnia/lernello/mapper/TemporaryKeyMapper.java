package ch.nova_omnia.lernello.mapper;

import java.util.Map;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ch.nova_omnia.lernello.dto.response.TemporaryKeySolveResDTO;

@Mapper(componentModel = "spring")
public interface TemporaryKeyMapper {
    @Mapping(target = "temporaryKeyMap", source = ".")
    TemporaryKeySolveResDTO toDTO(Map<String, UUID> temporaryKeyMap);
}
