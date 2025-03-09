package ch.nova_omnia.pm4.service;

import org.springframework.stereotype.Service;
import ch.nova_omnia.pm4.repository.InstructorRepository;
import ch.nova_omnia.pm4.model.data.Instructor;

import java.util.List;

/**
 * InstructorService is a service class that provides business logic for managing {@link Instructor} entities.
 */
@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;

    /**
     * Constructs an InstructorService with the specified {@link InstructorRepository}.
     * 
     * @param instructorRepository the repository to use for managing Instructor entities
     */
    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    /**
     * Retrieves all instructors.
     * 
     * @return a list of all instructors
     */
    public List<Instructor> getAllInstructors() {
        return (List<Instructor>) instructorRepository.findAll();
    }

    /**
     * Creates a new instructor.
     * 
     * @param instructor the instructor to create
     * @return the created instructor
     */
    public Instructor createInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }
}