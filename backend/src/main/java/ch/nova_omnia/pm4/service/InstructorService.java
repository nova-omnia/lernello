package ch.nova_omnia.pm4.service;

import ch.nova_omnia.pm4.model.data.Instructor;
import ch.nova_omnia.pm4.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class InstructorService extends AbstractCrudService<Instructor, Long> {

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    protected JpaRepository<Instructor, Long> getRepository() {
        return instructorRepository;
    }
}
