package ch.nova_omnia.lernello.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.service.LearningUnitService;
import ch.nova_omnia.lernello.dto.request.CreateLearningUnitDTO;

@RestController
@RequestMapping("/api/learning-units")
public class LearningUnitRestController {

    private final LearningUnitService learningUnitService;

    public LearningUnitRestController(LearningUnitService learningUnitService) {
        this.learningUnitService = learningUnitService;
    }

    @PostMapping
    public ResponseEntity<LearningUnit> createLearningUnit(@Valid @RequestBody CreateLearningUnitDTO request) {
        LearningUnit newUnit = learningUnitService.createLearningUnit(request.name());
        return new ResponseEntity<>(newUnit, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningUnit> getLearningUnit(@PathVariable UUID id) {
        Optional<LearningUnit> unit = learningUnitService.findById(id);
        return unit.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/blocks/order")
    public ResponseEntity<LearningUnit> updateBlockOrder(@PathVariable UUID id, @RequestBody List<UUID> blockIds) {
        // Load LearningUnit
        Optional<LearningUnit> opt = learningUnitService.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        LearningUnit unit = opt.get();
        // Reorder blocks as needed (use blockIds to rearrange)
        // Save changes via repository
        learningUnitService.save(unit);
        return ResponseEntity.ok(unit);
    }
}
