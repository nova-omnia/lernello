package ch.nova_omnia.pm4.api;

import org.springframework.web.bind.annotation.*;
import ch.nova_omnia.pm4.service.InstructorService;
import ch.nova_omnia.pm4.model.data.Instructor;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorRestController {
    private final InstructorService instructorService;

    public InstructorRestController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping
    public List<Instructor> getAllInstructors() {
        return instructorService.getAllInstructors();
    }

    @PostMapping
    public Instructor createInstructor(@RequestBody Instructor instructor) {
        return instructorService.createInstructor(instructor);
    }
}