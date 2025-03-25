package ch.nova_omnia.lernello.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.service.LearningUnitService;
import ch.nova_omnia.lernello.dto.request.CreateLearningUnitDTO;
import ch.nova_omnia.lernello.mapper.LearningUnitMapper;

@RestController
@RequestMapping("/api/learning-units")
@RequiredArgsConstructor
public class LearningUnitRestController {
    private final LearningUnitService learningUnitService;

    @PostMapping
    public ResponseEntity<LearningUnit> createLearningUnit(@Valid @RequestBody CreateLearningUnitDTO createLearningUnitDTO) {
        LearningUnit newUnit = learningUnitService.createLearningUnit(LearningUnitMapper.toEntity(createLearningUnitDTO));
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
