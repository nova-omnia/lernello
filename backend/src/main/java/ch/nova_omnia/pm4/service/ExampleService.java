package ch.nova_omnia.pm4.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ch.nova_omnia.pm4.model.data.ExampleEntity;
import ch.nova_omnia.pm4.repository.ExampleRepository;

@Service
public class ExampleService {

    ExampleRepository exampleRepository;

    public ExampleService(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    public List<ExampleEntity> createOneThenFindAll() {
        // create one first for fon
        ExampleEntity newEntitty = new ExampleEntity("example");
        exampleRepository.save(newEntitty);

        return exampleRepository.findAll();
    }

}
