package ch.nova_omnia.lernello.mapper.example;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import ch.nova_omnia.lernello.mapper.JsonNullableMapper;

@Mapper(uses = JsonNullableMapper.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, componentModel = "spring")
public interface EntityMapper {
    @Mapping(target = "id", ignore = true)
    void update(EntityUpdateDTO update, @MappingTarget EntityEx destination);

    default List<String> priceListToString(List<Double> price) {
        if (price == null) {
            return null;
        }
        return price.stream().map(String::valueOf).toList();
    }
}
