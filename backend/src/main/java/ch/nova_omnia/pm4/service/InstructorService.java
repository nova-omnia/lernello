package ch.nova_omnia.pm4.service;

import org.springframework.stereotype.Service;
import ch.nova_omnia.pm4.repository.InstructorRepository;
import ch.nova_omnia.pm4.model.data.Instructor;

import java.util.List;

@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public List<Instructor> getAllInstructors() {
        return (List<Instructor>) instructorRepository.findAll();
    }

    public Instructor createInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }
}