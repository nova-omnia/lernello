package ch.nova_omnia.lernello.mapper.example;

import java.util.List;

import org.openapitools.jackson.nullable.JsonNullable;

public record EntityUpdateDTO(
                              String name,
                              Integer quantity,
                              JsonNullable<String> description,
                              JsonNullable<String> manufacturer,
                              List<String> price
) {
}
