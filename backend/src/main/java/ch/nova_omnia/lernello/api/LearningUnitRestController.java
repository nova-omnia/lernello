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
    private final LearningUnitMapper learningUnitMapper;

    @PostMapping
    public ResponseEntity<LearningUnit> createLearningUnit(@Valid @RequestBody CreateLearningUnitDTO createLearningUnitDTO) {
        return new ResponseEntity<>(learningUnitService.createLearningUnit(learningUnitMapper.toEntity(createLearningUnitDTO)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningUnit> getById(@PathVariable UUID id) {
        return learningUnitService.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/blocks/order")
    public ResponseEntity<LearningUnit> updateBlockOrder(@PathVariable UUID id, @RequestBody List<UUID> blockIds) {
        return learningUnitService.updateBlockOrder(id, blockIds)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
