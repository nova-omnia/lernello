package ch.nova_omnia.pm4.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ch.nova_omnia.pm4.mapper.GenericMapper;
import ch.nova_omnia.pm4.service.AbstractCrudService;

// T: Entität, D: DTO, ID: Typ der ID (z.B. Long)
public abstract class AbstractCrudRestController<T, D, ID> {

    protected abstract AbstractCrudService<T, ID> getService();

    protected abstract GenericMapper<T, D> getMapper();

    @GetMapping
    public List<D> getAll() {
        List<T> entities = getService().findAll();
        return entities.stream().map(entity -> getMapper().toDto(entity)).collect(Collectors.toList());
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
