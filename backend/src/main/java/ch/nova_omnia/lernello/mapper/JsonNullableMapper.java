package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(componentModel = "spring")
public interface JsonNullableMapper {

    default <T> JsonNullable<T> wrap(T entity) {
        return JsonNullable.of(entity);
    }

    default <T> T unwrap(JsonNullable<T> jsonNullable) {
        return jsonNullable == null ? null : jsonNullable.orElse(null);
    }

    /**
    * Checks whether nullable parameter was passed explicitly.
    * @return true if value was set explicitly, false otherwise
    */
    @Condition
    default <T> boolean isPresent(JsonNullable<T> nullable) {
        return nullable != null && nullable.isPresent();
    }
}


// package ch.nova_omnia.lernello.mapper;

// import java.util.Optional;

// import org.mapstruct.Condition;
// import org.mapstruct.Mapper;
// // import org.openapitools.jackson.nullable.JsonNullable;

// @Mapper(componentModel = "spring")
// public interface JsonNullableMapper {

//     default <T> Optional<T> wrap(T entity) {
//         return Optional.of(entity);
//     }

//     default <T> T unwrap(Optional<T> jsonNullable) {
//         return jsonNullable == null ? null : jsonNullable.orElse(null);
//     }

//     /**
//     * Checks whether nullable parameter was passed explicitly.
//     * @return true if value was set explicitly, false otherwise
//     */
//     @Condition
//     default <T> boolean isPresent(Optional<T> nullable) {
//         return nullable != null && nullable.isPresent();
//     }
// }
