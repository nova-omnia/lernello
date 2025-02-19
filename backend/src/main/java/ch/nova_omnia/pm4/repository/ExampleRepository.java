package ch.nova_omnia.pm4.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ch.nova_omnia.pm4.model.data.ExampleEntity;

@Repository
public interface ExampleRepository extends CrudRepository<ExampleEntity, Long> {
    List<ExampleEntity> findAll();
}
