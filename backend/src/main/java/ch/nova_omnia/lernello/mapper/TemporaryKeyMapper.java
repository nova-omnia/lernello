package ch.nova_omnia.lernello.mapper;

import ch.nova_omnia.lernello.dto.response.TemporaryKeySolveResDTO;
import org.mapstruct.Mapper;

import java.util.Map;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TemporaryKeyMapper {
    TemporaryKeySolveResDTO toDTO(Map<String, UUID> temporaryKeyMap);
}
