package ch.nova_omnia.pm4.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import ch.nova_omnia.pm4.model.learningUnit.LearningUnit;
import ch.nova_omnia.pm4.service.LearningUnitService;
import ch.nova_omnia.pm4.dto.learningunit.LearningUnitCreateRequest;

@RestController
@RequestMapping("/api/learning-units")
public class LearningUnitRestController {

    private final LearningUnitService learningUnitService;

    public LearningUnitRestController(LearningUnitService learningUnitService) {
        this.learningUnitService = learningUnitService;
    }

    // Endpoint to create a new learning unit
    @PostMapping
    public ResponseEntity<LearningUnit> createLearningUnit(@Valid @RequestBody LearningUnitCreateRequest request) {
        LearningUnit newUnit = learningUnitService.createLearningUnit(request.getName());
        return new ResponseEntity<>(newUnit, HttpStatus.CREATED);
    }

    // Endpoint to retrieve a learning unit by ID
    @GetMapping("/{id}")
    public ResponseEntity<LearningUnit> getLearningUnit(@PathVariable Long id) {
        Optional<LearningUnit> unit = learningUnitService.findById(id);
        return unit.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
