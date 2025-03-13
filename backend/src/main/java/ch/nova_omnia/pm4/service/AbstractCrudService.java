package ch.nova_omnia.pm4.service;

import java.util.List;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AbstractCrudService<T, ID> {

    // Jede konkrete Service-Klasse muss ihr Repository bereitstellen.
    protected abstract JpaRepository<T, ID> getRepository();

    public List<T> findAll() {
        return getRepository().findAll();
    }

    public T findById(ID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found. ID: " + id));
    }

    public T create(T entity) {
        return getRepository().save(entity);
    }

    public void delete(ID id) {
        getRepository().deleteById(id);
    }
}
