package ch.nova_omnia.pm4.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.pm4.model.data.ExampleEntity;
import ch.nova_omnia.pm4.service.ExampleService;

@RestController
@RequestMapping("/api/example")
public class ExampleRestController {

    private final ExampleService exampleService;

    public ExampleRestController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @GetMapping("/test")
    public HttpStatus getTest() {

        // run the 
        List<ExampleEntity> entities = exampleService.createOneThenFindAll();
        System.out.println(entities);

        // just send OK to the client
        return HttpStatus.OK;
    }
}
