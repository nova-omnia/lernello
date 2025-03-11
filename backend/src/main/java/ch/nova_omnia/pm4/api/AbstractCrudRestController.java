package ch.nova_omnia.pm4.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ch.nova_omnia.pm4.mapper.GenericMapper;
import ch.nova_omnia.pm4.service.AbstractCrudService;

import java.util.List;
import java.util.stream.Collectors;

// T: Entität, D: DTO, ID: Typ der ID (z.B. Long)
public abstract class AbstractCrudRestController<T, D, ID> {

    protected abstract AbstractCrudService<T, ID> getService();

    protected abstract GenericMapper<T, D> getMapper();

    @GetMapping
    public List<D> getAll() {
        List<T> entities = getService().findAll();
        return entities.stream()
                .map(entity -> getMapper().toDto(entity))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public D getById(@PathVariable ID id) {
        T entity = getService().findById(id);
        return getMapper().toDto(entity);
    }

    @PostMapping
    public D create(@RequestBody T entity) {
        T created = getService().create(entity);
        return getMapper().toDto(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        getService().delete(id);
        return ResponseEntity.noContent().build();
    }
}
