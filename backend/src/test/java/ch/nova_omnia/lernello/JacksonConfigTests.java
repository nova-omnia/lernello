package ch.nova_omnia.lernello;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.nova_omnia.lernello.mapper.example.EntityEx;
import ch.nova_omnia.lernello.mapper.example.EntityMapper;
import ch.nova_omnia.lernello.mapper.example.EntityUpdateDTO;


@SpringBootTest
public class JacksonConfigTests {


    @Autowired
    private EntityMapper mapper;

    @Test
    void shouldUpdateAllEntitiesInEntityExExceptId() {
        EntityUpdateDTO update = new EntityUpdateDTO("Updated name", 2, JsonNullable.of("Updated description"), JsonNullable.of("UpdateCompany"));
        EntityEx destination = new EntityEx(1L, "RTX3080", 0, "Great GPU", "NVIDIA");
        EntityEx expected = new EntityEx(1L, "Updated name", 2, "Updated description", "UpdateCompany");

        mapper.update(update, destination);
        assertEquals(expected.getId(), destination.getId());
        assertEquals(expected.getDescription(), destination.getDescription());
        assertEquals(expected.getManufacturer(), destination.getManufacturer());
        assertEquals(expected.getName(), destination.getName());
        assertEquals(expected.getQuantity(), destination.getQuantity());
    }

    @Test
    void shouldUpdateOnlyNullableFieldsInEntityEx() {
        EntityUpdateDTO update = new EntityUpdateDTO(null, null, JsonNullable.of(null), JsonNullable.of(null));
        EntityEx destination = new EntityEx(1L, "RTX3080", 0, "Great GPU", "NVIDIA");
        EntityEx expected = new EntityEx(1L, "RTX3080", 0, null, null);
        mapper.update(update, destination);
        assertEquals(expected.getId(), destination.getId());
        assertEquals(expected.getDescription(), destination.getDescription());
        assertEquals(expected.getManufacturer(), destination.getManufacturer());
        assertEquals(expected.getName(), destination.getName());
        assertEquals(expected.getQuantity(), destination.getQuantity());
    }


    @Test
    void shouldNotUpdateAnyFieldInEntityEx() {
        EntityUpdateDTO update = new EntityUpdateDTO(null, null, JsonNullable.undefined(), JsonNullable.undefined());
        EntityEx destination = new EntityEx(1L, "RTX3080", 0, "Great GPU", "NVIDIA");
        EntityEx expected = new EntityEx(1L, "RTX3080", 0, "Great GPU", "NVIDIA");
        mapper.update(update, destination);
        assertEquals(expected.getId(), destination.getId());
        assertEquals(expected.getDescription(), destination.getDescription());
        assertEquals(expected.getManufacturer(), destination.getManufacturer());
        assertEquals(expected.getName(), destination.getName());
        assertEquals(expected.getQuantity(), destination.getQuantity());
    }

    @Test
    void shouldNotUpdateAnyFieldInEntityEx_2() {
        EntityUpdateDTO update = new EntityUpdateDTO(null, null, null, null);
        EntityEx destination = new EntityEx(1L, "RTX3080", 0, "Great GPU", "NVIDIA");
        EntityEx expected = new EntityEx(1L, "RTX3080", 0, "Great GPU", "NVIDIA");
        mapper.update(update, destination);
        assertEquals(expected.getId(), destination.getId());
        assertEquals(expected.getDescription(), destination.getDescription());
        assertEquals(expected.getManufacturer(), destination.getManufacturer());
        assertEquals(expected.getName(), destination.getName());
        assertEquals(expected.getQuantity(), destination.getQuantity());
    }
}
