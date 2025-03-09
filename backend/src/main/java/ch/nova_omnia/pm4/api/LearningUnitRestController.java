package ch.nova_omnia.pm4.api;

import org.springframework.web.bind.annotation.*;
import ch.nova_omnia.pm4.service.LearningUnitService;
import ch.nova_omnia.pm4.model.data.LearningUnit;

@RestController
@RequestMapping("/api/learning-units")
public class LearningUnitRestController {
    private final LearningUnitService learningUnitService;

    public LearningUnitRestController(LearningUnitService learningUnitService) {
        this.learningUnitService = learningUnitService;
    }

}