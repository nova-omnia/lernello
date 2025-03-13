package ch.nova_omnia.pm4.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.pm4.dto.InstructorDto;
import ch.nova_omnia.pm4.mapper.InstructorMapper;
import ch.nova_omnia.pm4.model.data.Instructor;
import ch.nova_omnia.pm4.service.InstructorService;

@RestController
@RequestMapping("/api/instructors")
public class InstructorRestController extends AbstractCrudRestController<Instructor, InstructorDto, Long> {
    @Autowired
    private InstructorService instructorService;

    @Autowired
    private InstructorMapper instructorMapper;

    @Override
    protected InstructorService getService() {
        return instructorService;
    }

    @Override
    protected InstructorMapper getMapper() {
        return instructorMapper;
    }
}
