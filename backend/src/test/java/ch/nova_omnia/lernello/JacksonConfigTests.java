/* package ch.nova_omnia.lernello;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

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
        EntityUpdateDTO update = new EntityUpdateDTO("Updated name", 2, JsonNullable.of("Updated description"), JsonNullable.of("UpdateCompany"), new ArrayList<>(List.of("100.0")));
        EntityEx destination = new EntityEx(1L, "RTX3080", 0, "Great GPU", "NVIDIA", new ArrayList<>(List.of(50.0)));
        EntityEx expected = new EntityEx(1L, "Updated name", 2, "Updated description", "UpdateCompany", List.of(100.0));

        mapper.update(update, destination);
        assertEquals(expected.getId(), destination.getId());
        assertEquals(expected.getDescription(), destination.getDescription());
        assertEquals(expected.getManufacturer(), destination.getManufacturer());
        assertEquals(expected.getName(), destination.getName());
        assertEquals(expected.getQuantity(), destination.getQuantity());
        assertEquals(expected.getPrice(), destination.getPrice());
    }

    @Test
    void shouldUpdateOnlyNullableFieldsInEntityEx() {
        EntityUpdateDTO update = new EntityUpdateDTO(null, null, JsonNullable.of(null), JsonNullable.of(null), null);
        EntityEx destination = new EntityEx(1L, "RTX3080", 0, "Great GPU", "NVIDIA", new ArrayList<>(List.of(50.0)));
        EntityEx expected = new EntityEx(1L, "RTX3080", 0, null, null, List.of(50.0));

        mapper.update(update, destination);
        assertEquals(expected.getId(), destination.getId());
        assertEquals(expected.getDescription(), destination.getDescription());
        assertEquals(expected.getManufacturer(), destination.getManufacturer());
        assertEquals(expected.getName(), destination.getName());
        assertEquals(expected.getQuantity(), destination.getQuantity());
        assertEquals(expected.getPrice(), destination.getPrice());
    }

    @Test
    void shouldNotUpdateAnyFieldInEntityEx() {
        EntityUpdateDTO update = new EntityUpdateDTO(null, null, JsonNullable.undefined(), JsonNullable.undefined(), null);
        EntityEx destination = new EntityEx(1L, "RTX3080", 0, "Great GPU", "NVIDIA", new ArrayList<>(List.of(50.0)));
        EntityEx expected = new EntityEx(1L, "RTX3080", 0, "Great GPU", "NVIDIA", List.of(50.0));

        mapper.update(update, destination);
        assertEquals(expected.getId(), destination.getId());
        assertEquals(expected.getDescription(), destination.getDescription());
        assertEquals(expected.getManufacturer(), destination.getManufacturer());
        assertEquals(expected.getName(), destination.getName());
        assertEquals(expected.getQuantity(), destination.getQuantity());
        assertEquals(expected.getPrice(), destination.getPrice());
    }

    @Test
    void shouldNotUpdateAnyFieldInEntityEx_2() {
        EntityUpdateDTO update = new EntityUpdateDTO(null, null, null, null, null);
        EntityEx destination = new EntityEx(1L, "RTX3080", 0, "Great GPU", "NVIDIA", new ArrayList<>(List.of(50.0)));
        EntityEx expected = new EntityEx(1L, "RTX3080", 0, "Great GPU", "NVIDIA", List.of(50.0));

        mapper.update(update, destination);
        assertEquals(expected.getId(), destination.getId());
        assertEquals(expected.getDescription(), destination.getDescription());
        assertEquals(expected.getManufacturer(), destination.getManufacturer());
        assertEquals(expected.getName(), destination.getName());
        assertEquals(expected.getQuantity(), destination.getQuantity());
        assertEquals(expected.getPrice(), destination.getPrice());
    }
}
 */